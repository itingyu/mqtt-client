package com.canyue.mqtt.core.client.impl;

import com.canyue.mqtt.core.*;
import com.canyue.mqtt.core.EventSource.ClientStatusEventSource;
import com.canyue.mqtt.core.EventSource.MessageEventSource;
import com.canyue.mqtt.core.callback.ClientCallback;
import com.canyue.mqtt.core.client.IMqttClient;
import com.canyue.mqtt.core.exception.MqttException;
import com.canyue.mqtt.core.exception.MqttIllegalArgumentException;
import com.canyue.mqtt.core.exception.MqttPersistenceException;
import com.canyue.mqtt.core.exception.MqttStartFailedException;
import com.canyue.mqtt.core.network.INetworkModule;
import com.canyue.mqtt.core.network.impl.TcpModule;
import com.canyue.mqtt.core.packet.*;
import com.canyue.mqtt.core.persistence.IPersistence;
import com.canyue.mqtt.core.util.TopicUtils;
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
    private MessageEventSource messageEventSource;
    private ClientStatusEventSource clientStatusEventSource;
    private String host="127.0.0.1";
    private int port=1883;
    private SenderThread senderThread ;
    private ReceiverThread receiverThread;
    private PingThread pingThread;
    private IPersistence persistence;
    private Object pingLock=new Object();
    private ConnectConfig connectConfig;
    private int max_reconnect_times = 5;
    private int reconnect_count=0;
    private boolean runState = false;

    private synchronized static int getMsgId() {
        msgId++;
        return msgId-1;
    }

    public void start() throws MqttStartFailedException {
        Thread.currentThread().setName("MainThread");
        networkModule = new TcpModule(host,port);
        logger.debug("正在与服务器{}建立连接。。。");
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
            logger.error("socket连接异常,请检查你的网络配置");
            throw new MqttStartFailedException("socket启动失败",e);
        }
    }


    public MqttClient setMessageEventSource(MessageEventSource messageEventSource) {
        this.messageEventSource=messageEventSource;
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
    public void unsubscribe(String[] topicsFilters) throws MqttException {
        for(int i=0;i<topicsFilters.length;i++){
            TopicUtils.validateTopicFilter(topicsFilters[i]);
        }
        int msgId=getMsgId();
        logger.debug("正在生成unsubscribe报文:" +
                "\tmsgId:{};",msgId);
        UnsubscribePacket unsubscribePacket = new UnsubscribePacket(
                topicsFilters,msgId
                );
      messageQueue.handleSendMsg(unsubscribePacket);
        logger.info("msgId:{},unsubscribe报文已加入队列!",msgId);
    }
    public void subscribe(String[] topicsFilters,int[] qosList) throws MqttException {
        for(int i=0;i<topicsFilters.length;i++){
            TopicUtils.validateTopicFilter(topicsFilters[i]);
        }
        int msgId=getMsgId();
        logger.debug("正在生成unsubscribe报文!");
        SubscribePacket subscribePacket=new SubscribePacket(
                topicsFilters,
                qosList,msgId);
       messageQueue.handleSendMsg(subscribePacket);
        logger.info("msgId:{},subscribe报文已加入队列!",msgId);
    }

    public void disconnect() throws MqttException {
        logger.debug("正在生成disconnect报文!");
        DisconnectPacket disconnectPacket = new DisconnectPacket();
        messageQueue.handleSendMsg(disconnectPacket);
        logger.info("disconnect报文已加入队列!");
        persistence.save("msgId",msgId);
        persistence.close();
        executorService.shutdownNow();
        try {
            executorService.awaitTermination(2,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new MqttException(e);
        }

    }
    public void connect(String username,String password,String clientId,Message willMessage,int keepAlive,boolean cleanSession) throws MqttException {
        logger.debug("正在生成connect报文!");
        connectConfig=new ConnectConfig(clientId,willMessage,username,password,cleanSession,keepAlive);
        ConnectPacket connectPacket = new ConnectPacket(clientId
                ,willMessage,username,password,cleanSession,keepAlive);
        //连接报文
        Object o;
        if((o=persistence.find("msgId"))!=null){
            msgId= (int) o;
        }
        messageQueue=new MessageQueue(persistence,connectConfig);
        messageQueue.setClientCallback(new ClientCallbackImpl(this.messageQueue));

        messageQueue.setMessageEventSource(messageEventSource);
        messageQueue.setClientStatusEventSource(clientStatusEventSource);

        senderThread.setMessageQueue(messageQueue);
        receiverThread.setMessageQueue(messageQueue);
        pingThread.setMessageQueue(messageQueue);
        pingThread.setKeepAlive(keepAlive);

        executorService = Executors.newFixedThreadPool(3);
        executorService.submit(senderThread);
        executorService.submit(receiverThread);
        executorService.submit(pingThread);

        messageQueue.handleSendMsg(connectPacket);
        logger.info("connect报文已加入队列!");
    }
    private void connect(ConnectConfig connectConfig) throws MqttException {
        this.connect(connectConfig.getUserName(),connectConfig.getPassword(),connectConfig.getClientId(),connectConfig.getWillMessage(),connectConfig.getKeepAlive(),connectConfig.isCleanSession());
    }

    public void publish(String topicName, byte[] payload, int qos, boolean isRetain) throws MqttIllegalArgumentException, MqttPersistenceException {
        TopicUtils.validateTopicName(topicName);
        if(qos<0||qos>2){
            throw new MqttIllegalArgumentException("不合法的消息服务质量!");
        }
        Message msg = new Message(topicName);
        msg.setPayload(payload);
        msg.setQos(qos);
        msg.setRetain(isRetain);
        if(qos>0){
            msg.setMsgId(getMsgId());
        }
        logger.debug("正在生成publish报文:" +
                "\tQos:{}"+
                "\tmsgId:{};",msg.getQos(),msg.getMsgId());
        PublishPacket publishMsg = new PublishPacket(msg);
        messageQueue.handleSendMsg(publishMsg);
        logger.info("msgId:{},publish报文已加入队列!",msg.getMsgId());
    }
    class ClientCallbackImpl implements ClientCallback {
        private MessageQueue messageQueue;
        public ClientCallbackImpl(MessageQueue messageQueue) {
            this.messageQueue=messageQueue;
        }

        @Override
        public void connectCompeted() {
            runState = true;
            this.messageQueue.connectCompeted();
            logger.info("连接成功!");
        }

        public void reconnect() {
            if(reconnect_count<max_reconnect_times){
                reconnect_count++;
                try {

                    persistence.close();
                    executorService.shutdownNow();
                    try {
                        executorService.awaitTermination(2,TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info("3秒之后进行第{}次自动重连！",reconnect_count);
                    Thread.sleep(3*1000);
                    start();
                    connect(connectConfig);
                }catch (MqttException | InterruptedException e) {
                    logger.error("第{}次自动重连失败",reconnect_count,e);
                }
            }else {
                logger.info("重连失败，请检查你的连接配置参数！");
                try {
                    persistence.close();
                } catch (MqttPersistenceException e) {
                    e.printStackTrace();
                }
                executorService.shutdownNow();
                try {
                    executorService.awaitTermination(2,TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void shutdown() {
            if(runState){
                try {
                    persistence.save("msgId",msgId);
                    persistence.close();
                } catch (MqttPersistenceException e) {
                    logger.error("发生错误，未正常关闭，可能会丢失数据！");
                }
                executorService.shutdownNow();
                try {
                    executorService.awaitTermination(2,TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                }
                runState=false;
                this.messageQueue.shutdown();
            }
        }
    }

    public MqttClient setClientStatusEventSource(ClientStatusEventSource clientStatusEventSource) {
        this.clientStatusEventSource = clientStatusEventSource;
        return this;
    }
}
