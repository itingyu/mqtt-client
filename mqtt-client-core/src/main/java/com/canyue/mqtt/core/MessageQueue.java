package com.canyue.mqtt.core;

import com.canyue.mqtt.core.EventSource.ClientStatusEventSource;
import com.canyue.mqtt.core.EventSource.MessageEventSource;
import com.canyue.mqtt.core.callback.ClientCallback;
import com.canyue.mqtt.core.event_object.ClientStatusEvent;
import com.canyue.mqtt.core.event_object.MessageEvent;
import com.canyue.mqtt.core.exception.MqttPersistenceException;
import com.canyue.mqtt.core.listener.ClientStatusListener;
import com.canyue.mqtt.core.listener.MessageReceivedListener;
import com.canyue.mqtt.core.packet.*;
import com.canyue.mqtt.core.persistence.IPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class MessageQueue {
    //需要发送的消息
    private LinkedList<BasePacket> willSendQueue=new LinkedList<BasePacket>();
    //待确认的消息，如果没收到确认，就重发
    private LinkedList<BasePacket> maybeReSendQueue=new LinkedList<BasePacket>();
    private ConnectConfig connectConfig;
    private final Object lock=new Object();
    private MessageEventSource messageEventSource;
    private ClientStatusEventSource clientStatusEventSource;
    private ClientCallback clientCallback;
    private static Logger logger= LoggerFactory.getLogger(MessageQueue.class);
    private IPersistence persistence=null;
    private boolean connected=false;

    public MessageQueue(IPersistence persistence ,ConnectConfig connectConfig){
        this.persistence=persistence;
       this.connectConfig = connectConfig;
    }

    public  void addLast(BasePacket packet){
       synchronized (lock){
           this.willSendQueue.offerLast(packet);
           lock.notify();
       }
    }
    public  void addFirst(BasePacket packet){
        synchronized (lock){
            this.willSendQueue.offerFirst(packet);
            lock.notify();
        }
    }
    public  BasePacket getSend() throws InterruptedException {
        BasePacket packet=null;
       synchronized (lock){
           while(packet==null){
               if(!willSendQueue.isEmpty()){
                   packet= willSendQueue.pollFirst();
               }else {
                  lock.wait();
               }
           }
       }
        return packet;
    }
    //connect  subscribe unsubscribe ping disconnect
    ////publish publishRec publishRel publishComp publishAck
    public void handleSendMsg(BasePacket basePacket) throws MqttPersistenceException {
        if(basePacket instanceof PublishPacket){
            Message msg = ((PublishPacket) basePacket).getMessage();

            switch (msg.getQos()){
                case 1:
                    persistence.save(msg.getMsgId()+".p",msg);
                    break;
                case 2:
                    persistence.save(msg.getMsgId()+".p",msg);
                    break;
                default:break;
            }
            this.addLast(basePacket);
        }else if(basePacket instanceof ConnectPacket){
            this.addFirst(basePacket);
        }else if(basePacket instanceof PingReqPacket){
           if (connected){
               this.addFirst(basePacket);
           }
        }else {
            this.addLast(basePacket);
        }
    }
    //conAck subscribeAck unsubscribeAck pingResp
    //publish publishRec publishRel publishComp publishAck
    public void handleReceivedMsg(BasePacket basePacket) throws MqttPersistenceException {
        if(basePacket instanceof PublishPacket){
            logger.info("接收到一个publish报文,正在处理中....");
            Message msg = ((PublishPacket) basePacket).getMessage();
            if(messageEventSource!=null){
                messageEventSource.notifyListenerEvent(new MessageEvent(messageEventSource,msg));
            }
            switch (msg.getQos()){
                case 1:
                    addLast(new PubAckPacket(msg.getMsgId()));
                break;
                case 2:
                    persistence.save(msg.getMsgId()+".prec",msg.getMsgId());
                    addLast(new PubRecPacket(msg.getMsgId()));
                break;
                default:break;
            }
        }else if(basePacket instanceof PubRecPacket){
            PubRecPacket pubRecPacket = (PubRecPacket) basePacket;
            persistence.remove(pubRecPacket.getMsgId()+".p");
            persistence.save(pubRecPacket.getMsgId()+".prel",pubRecPacket.getMsgId());
            addLast(new PubRelPacket(pubRecPacket.getMsgId()));
        }else if(basePacket instanceof PubRelPacket){
            PubRelPacket pubRelPacket = (PubRelPacket) basePacket;
            persistence.remove(pubRelPacket.getMsgId()+".prec");
            addLast(new PubCompPacket(pubRelPacket.getMsgId()));
        }else if(basePacket instanceof PubCompPacket){
            persistence.remove(((PubCompPacket) basePacket).getMsgId()+".prel");
        }else if(basePacket instanceof PubAckPacket){
            persistence.remove(((PubAckPacket) basePacket).getMsgId()+".p");
        }else if(basePacket instanceof ConnectAckPacket){
            int returnCode = ((ConnectAckPacket) basePacket).getReturnCode();
            if(returnCode==0){
                connected=true;
                this.clientCallback.connectCompeted();
                if(connectConfig.isCleanSession()){
                    try {
                        persistence.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    this.persistence.open(connectConfig.getClientId()+"");

                } catch (MqttPersistenceException e) {
                    throw new MqttPersistenceException("persistence 打开异常!",e);
                }
                if(!connectConfig.isCleanSession()) {
                    List<BasePacket> list = this.persistence.getAllNeed2Retry();
                    for (BasePacket bp : list) {
                        this.addLast(bp);
                    }
                }
            }else {
                connected=false;
                logger.info("连接失败,return code:{}",returnCode);
                clientCallback.reconnect();
            }
        }
        logger.info("收到一个{}报文,正在处理中。。。",basePacket.getType());
    }

    public ClientCallback getClientCallback() {
        return clientCallback;
    }

    public void setClientCallback(ClientCallback clientCallback) {
        this.clientCallback = clientCallback;
    }
    public void shutdown(){
        if(clientStatusEventSource!=null){
            clientStatusEventSource.notifyListenerEvent(new ClientStatusEvent(clientStatusEventSource,ClientStatusEvent.SHUTDOWN));
        }
    }
    public void connectCompeted(){
        if(clientStatusEventSource!=null){
            clientStatusEventSource.notifyListenerEvent(new ClientStatusEvent(clientStatusEventSource,ClientStatusEvent.RUN));
        }
    }

    public void setMessageReceivedListener(MessageReceivedListener messageReceivedListener) {
        if(messageReceivedListener!=null){
            if(messageEventSource==null){
                messageEventSource = new MessageEventSource();
            }
            this.messageEventSource.setListener(messageReceivedListener);
        }
    }

    public void setClientStatusListener(ClientStatusListener clientStatusListener) {
        if(clientStatusListener!=null){
            if(clientStatusEventSource==null){
                clientStatusEventSource=new ClientStatusEventSource();
            }
            this.clientStatusEventSource.setListener(clientStatusListener);
        }
    }
}
