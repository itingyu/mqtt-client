package com.canyue.mqtt.core.client.impl;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.MessageShower;
import com.canyue.mqtt.core.ReceiverThread;
import com.canyue.mqtt.core.SenderThread;
import com.canyue.mqtt.core.client.IMqttClient;
import com.canyue.mqtt.core.network.INetworkModule;
import com.canyue.mqtt.core.network.impl.TcpModule;
import com.canyue.mqtt.core.packet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MqttClient implements IMqttClient {
    ConcurrentLinkedQueue<BasePacket> clq;
    INetworkModule tcp;
    ScheduledExecutorService scheduledThreadPool;
    private static int msgId=1;
    private static Logger logger= LoggerFactory.getLogger(MqttClient.class);
    MessageShower messageShower;
    private String host="127.0.0.1";
    private int port=1883;
    public void start(){
        Thread.currentThread().setName("MainThread");
        clq = new ConcurrentLinkedQueue();
        tcp = new TcpModule(host,port);
        logger.debug("正在与服务器{}建立连接。。。");
        scheduledThreadPool = Executors.newScheduledThreadPool(5);
        try {
            tcp.start();
            scheduledThreadPool.schedule(new SenderThread(tcp.getOutputStream(),clq),0, TimeUnit.SECONDS);
            scheduledThreadPool.schedule(new ReceiverThread(tcp.getInputStream(),clq,messageShower),0,TimeUnit.SECONDS);
            scheduledThreadPool.schedule(new Runnable() {
                public void run() {
                    while (true){
                        try {
                            Thread.sleep(60*1000);
                            ping();
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            },1,TimeUnit.SECONDS);
            logger.info("连接已建立。。。");
            scheduledThreadPool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                tcp.stop();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public void publish(Message msg) throws IOException {
        msg.setMsgId(msgId);
        msgId++;
        logger.debug("正在生成publish报文:" +
                "\tmsgId:{};",msg);
        PublishPacket publishMsg = new PublishPacket(msg);
        clq.offer(publishMsg);
        logger.info("msgId:{},publish报文已加入队列!",msgId);
    }

    public MqttClient setMessageShower(MessageShower messageShower) {
        this.messageShower=messageShower;
        return this;
    }
    public MqttClient setHost(String host){
        this.host=host;
        return this;
    }
    public MqttClient setPort(int port){
        this.port=port;
        return this;
    }
    public void unsubscribe(String[] topics) throws IOException {
        logger.debug("正在生成unsubscribe报文:" +
                "\tmsgId:{};",msgId);
        UnsubscribePacket mqttUnsubscribeMsg = new UnsubscribePacket(
               topics,
                msgId);
        clq.offer(mqttUnsubscribeMsg);
        logger.info("msgId:{},unsubscribe报文已加入队列!",msgId);
    }
    public void subscribe(String[] topics,int[] qosList) throws IOException {
        logger.debug("正在生成unsubscribe报文!");
        SubscribePacket mqttSubscribeMsg=new SubscribePacket(
                topics,
                qosList,msgId);
        msgId++;
        clq.offer(mqttSubscribeMsg);
        logger.info("msgId:{},subscribe报文已加入队列!",msgId);
    }

    public void disconnect() throws IOException {
        logger.debug("正在生成disconnect报文!");
        BasePacket disconnectMsg = new DisconnectPacket();
        clq.offer(disconnectMsg);
        logger.info("disconnect报文已加入队列!",msgId);
    }
    public void connect(String username,String password,String clientId,Message willMessage,int keepAlive,boolean cleanSession) throws IOException {
        logger.debug("正在生成connect报文!");
        BasePacket mqttConnectMsg = new ConnectPacket(clientId
                ,willMessage,username,password,cleanSession,keepAlive);
        //连接报文
        clq.offer(mqttConnectMsg);
        logger.info("disconnect报文已加入队列!",msgId);
    }
    public void ping() throws IOException {
        BasePacket pingReq = new PingReqPacket();
       clq.offer(pingReq);
       logger.debug("ping报文已加入队列");
    }
}
