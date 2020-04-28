package com.canyue.mqtt.core.client.impl;

import com.canyue.mqtt.core.*;
import com.canyue.mqtt.core.client.IMqttClient;
import com.canyue.mqtt.core.exception.MqttStartFailedException;
import com.canyue.mqtt.core.network.INetworkModule;
import com.canyue.mqtt.core.network.impl.TcpModule;
import com.canyue.mqtt.core.packet.*;
import com.canyue.mqtt.core.persistence.IPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.*;

public class MqttClient implements IMqttClient {
    private MessageQueue messageQueue;
    private INetworkModule networkModule;
    private ExecutorService executorService;
    private static int msgId=1;
    private static Logger logger= LoggerFactory.getLogger(MqttClient.class);
    private MessageShower messageShower;
    private String host="127.0.0.1";
    private int port=1883;
    private SenderThread senderThread ;
    private ReceiverThread receiverThread;
    private PingThread pingThread;
    private IPersistence persistence;
    private Object pingLock=new Object();

    private synchronized static int getMsgId() {
        msgId++;
        return msgId-1;
    }

    public void start() throws MqttStartFailedException {
        Thread.currentThread().setName("MainThread");
        networkModule = new TcpModule(host,port);
        logger.debug("正在与服务器{}建立连接。。。");
        executorService = Executors.newFixedThreadPool(3);
        try {
            networkModule.start();
            senderThread=new SenderThread(networkModule.getOutputStream());
            senderThread.setPingLock(pingLock);
            receiverThread=new ReceiverThread(networkModule.getInputStream());
            pingThread=new PingThread();
            pingThread.setPingLock(pingLock);
            logger.info("socket连接已建立。。。");

        } catch (IOException e) {
            try {
                networkModule.stop();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            logger.error("socket连接异常",e);
            throw new MqttStartFailedException("socket启动失败");
        }
    }
    public void publish(Message msg) throws Exception {
        if(msg.getQos()>0){
            msg.setMsgId(getMsgId());
        }
        logger.debug("正在生成publish报文:" +
                "\tQos:{}"+
                "\tmsgId:{};",msg.getQos(),msg.getMsgId());
        PublishPacket publishMsg = new PublishPacket(msg);
        messageQueue.handleSendMsg(publishMsg);
        logger.info("msgId:{},publish报文已加入队列!",msg.getMsgId());
    }

    public MqttClient setMessageShower(MessageShower messageShower) {
        this.messageShower=messageShower;
        return this;
    }
    public MqttClient setHost(String host){
        this.host=host;
        return this;
    }
    public MqttClient setNetworkModule(INetworkModule networkModule){
        this.networkModule = networkModule;
        return this;
    }
    public MqttClient setPort(int port){
        this.port=port;
        return this;
    }
    public MqttClient setPersistence(IPersistence persistence){
        this.persistence=persistence;
        return this;
    }
    public void unsubscribe(String[] topics) throws Exception {
        int msgId=getMsgId();
        logger.debug("正在生成unsubscribe报文:" +
                "\tmsgId:{};",msgId);
        UnsubscribePacket unsubscribePacket = new UnsubscribePacket(
               topics,msgId
                );
      messageQueue.handleSendMsg(unsubscribePacket);
        logger.info("msgId:{},unsubscribe报文已加入队列!",msgId);
    }
    public void subscribe(String[] topics,int[] qosList) throws Exception {
        int msgId=getMsgId();
        logger.debug("正在生成unsubscribe报文!");
        SubscribePacket subscribePacket=new SubscribePacket(
                topics,
                qosList,msgId);
       messageQueue.handleSendMsg(subscribePacket);
        logger.info("msgId:{},subscribe报文已加入队列!",msgId);
    }

    public void disconnect() throws Exception {
        logger.debug("正在生成disconnect报文!");
        DisconnectPacket disconnectPacket = new DisconnectPacket();
       messageQueue.handleSendMsg(disconnectPacket);
        logger.info("disconnect报文已加入队列!");
        persistence.save("msgId",msgId);
        persistence.close();
        executorService.shutdownNow();
        executorService.awaitTermination(2,TimeUnit.SECONDS);

    }
    public void connect(String username,String password,String clientId,Message willMessage,int keepAlive,boolean cleanSession) throws Exception {
        logger.debug("正在生成connect报文!");
        ConnectConfig connectConfig=new ConnectConfig(clientId,willMessage,username,password,cleanSession,keepAlive);
        ConnectPacket connectPacket = new ConnectPacket(clientId
                ,willMessage,username,password,cleanSession,keepAlive);
        //连接报文
        Object o;
        if((o=persistence.find("msgId"))!=null){
            msgId= (int) o;
        }
        messageQueue=new MessageQueue(persistence,connectConfig);
        messageQueue.setMessageShower(messageShower);
        senderThread.setMessageQueue(messageQueue);
        receiverThread.setMessageQueue(messageQueue);
        pingThread.setMessageQueue(messageQueue);
        pingThread.setKeepAlive(keepAlive);
        try {

//            senderFuture = scheduledThreadPool.scheduleAtFixedRate(senderThread, 0, TimeUnit.SECONDS);
//            receiverFuture = scheduledThreadPool.schedule(receiverThread,0,TimeUnit.SECONDS);
//            pingFuture = scheduledThreadPool.schedule(pingThread,0,TimeUnit.SECONDS);
             executorService.submit(senderThread);
             executorService.submit(receiverThread);
             executorService.submit(pingThread);
        } catch (Exception e) {
            executorService.shutdownNow();
            executorService.awaitTermination(2,TimeUnit.SECONDS);
            try {
                networkModule.stop();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            logger.error("mqtt连接异常",e);
            throw new Exception("mqtt连接异常");
        }
        messageQueue.handleSendMsg(connectPacket);
        logger.info("connect报文已加入队列!");
    }
}
