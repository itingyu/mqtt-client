package com.canyue.mqtt.core;

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
    private MessageShower messageShower;
    private static Logger logger= LoggerFactory.getLogger(MessageQueue.class);
    private IPersistence persistence=null;
    private boolean connected=false;

    public MessageQueue(IPersistence persistence ,ConnectConfig connectConfig) throws Exception {
        this.persistence=persistence;
       this.connectConfig = connectConfig;
        if(connectConfig.isCleanSession()){
            try {
                persistence.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            this.persistence.open(connectConfig.getClientId()+"");

        } catch (Exception e) {
            logger.error("persistence 打开异常!");
            throw new Exception();
        }
        if(!connectConfig.isCleanSession()) {
            List<BasePacket> list = this.persistence.getAllNeed2Retry();

            for (BasePacket basePacket : list) {
                this.addLast(basePacket);
            }
        }
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
    public  BasePacket getSend(){
        BasePacket packet=null;
       synchronized (lock){
           while(packet==null){
               if(!willSendQueue.isEmpty()){
                   packet= willSendQueue.pollFirst();
               }else {
                   try {
                       lock.wait();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }
       }
        return packet;
    }
    //connect  subscribe unsubscribe ping disconnect
    ////publish publishRec publishRel publishComp publishAck
    public void handleSendMsg(BasePacket basePacket) throws Exception {
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
    public void handleReceivedMsg(BasePacket basePacket) throws Exception {
        if(messageShower!=null){
            messageShower.notifyListenerEvent(new PacketReceived(basePacket));
        }
        if(basePacket instanceof PublishPacket){
            logger.info("接收到一个publish报文,正在处理中....");
            Message msg = ((PublishPacket) basePacket).getMessage();

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
            connected=true;
        }
        logger.info("收到一个{}报文,正在处理中。。。",basePacket.getType());
    }

    public void setMessageShower(MessageShower messageShower) {
        this.messageShower = messageShower;
    }
}
