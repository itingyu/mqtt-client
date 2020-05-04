package com.canyue.mqtt.core.client.impl;

import com.canyue.mqtt.core.*;
import com.canyue.mqtt.core.callback.ClientCallback;
import com.canyue.mqtt.core.client.IMqttClient;
import com.canyue.mqtt.core.exception.MqttException;
import com.canyue.mqtt.core.exception.MqttIllegalArgumentException;
import com.canyue.mqtt.core.exception.MqttPersistenceException;
import com.canyue.mqtt.core.exception.MqttStartFailedException;
import com.canyue.mqtt.core.listener.ClientStatusListener;
import com.canyue.mqtt.core.listener.MessageReceivedListener;
import com.canyue.mqtt.core.network.INetworkModule;
import com.canyue.mqtt.core.network.impl.TcpModule;
import com.canyue.mqtt.core.packet.*;
import com.canyue.mqtt.core.persistence.IPersistence;
import com.canyue.mqtt.core.utils.TopicUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author canyue
 */
public class MqttClient implements IMqttClient {
    private static int msgId=1;
    private int maxReconnectTimes = 5;
    private int reconnectCount =0;
    private static Logger logger= LoggerFactory.getLogger(MqttClient.class);
    private Object pingLock=new Object();
    private boolean runState = false;

    private SenderThread senderThread ;
    private ReceiverThread receiverThread;
    private PingThread pingThread;
    private ExecutorService executorService;
    private ConnectConfig connectConfig;

     String host;
    int port;
    private MessageQueue messageQueue;
    private INetworkModule networkModule;
    IPersistence persistence;
    MessageReceivedListener messageReceivedListener;
    ClientStatusListener clientStatusListener;

    private synchronized static int getMsgId() {
       int _msgId = msgId;
       if(++msgId>65535){
           msgId=1;
       }
        return _msgId;
    }
    public MqttClient(){
        this(new Builder());
    }
     MqttClient(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.persistence =builder.persistence;
        this.messageReceivedListener = builder.messageReceivedListener;
        this.clientStatusListener = builder.clientStatusListener;

    }

    @Override
    public void start() throws MqttStartFailedException {
        Thread.currentThread().setName("MainThread");
        networkModule = new TcpModule(host,port);
        logger.debug("正在与服务器{}建立连接。。。");
        try {
            logger.info("目标主机地址 {}:{}",host,port);
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

    @Override
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
    @Override
    public void subscribe(String[] topicsFilters, int[] qosList) throws MqttException {
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

    @Override
    public void disconnect() throws MqttException {
        logger.debug("正在生成disconnect报文!");
        DisconnectPacket disconnectPacket = new DisconnectPacket();
        messageQueue.handleSendMsg(disconnectPacket);
        logger.info("disconnect报文已加入队列!");
        try {
            networkModule.stop();
        } catch (IOException e) {
            logger.error("socket关闭异常",e);
        }

    }
    @Override
    public void connect(String username, String password, String clientId, Message willMessage, int keepAlive, boolean cleanSession) throws MqttException {
        logger.debug("正在生成connect报文!");
        connectConfig=new ConnectConfig(clientId,willMessage,username,password,cleanSession,keepAlive);
        ConnectPacket connectPacket = new ConnectPacket(clientId
                ,willMessage,username,password,cleanSession,keepAlive);
        //连接报文
        Object o;
        initMsgId();
        messageQueue=new MessageQueue(persistence,connectConfig);
        messageQueue.setClientCallback(new ClientCallbackImpl(this.messageQueue));

        messageQueue.setMessageReceivedListener(messageReceivedListener);
        messageQueue.setClientStatusListener(clientStatusListener);

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

    @Override
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

        @Override
        public synchronized void reconnect() {
            if(reconnectCount<maxReconnectTimes){
                reconnectCount++;
                try {

                    persistence.close();
                    executorService.shutdownNow();
                    try {
                        executorService.awaitTermination(2,TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        networkModule.stop();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    logger.info("3秒之后进行第{}次自动重连！",reconnectCount);
                    Thread.sleep(3*1000);
                    start();
                    connect(connectConfig);
                }catch (MqttException | InterruptedException e) {
                    logger.error("第{}次自动重连失败",reconnectCount,e);
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
        @Override
        public void shutdown() {
            synchronized (this){
                if(runState){
                    try {
                        saveMsgId();
                        persistence.close();
                    } catch (MqttPersistenceException e) {
                        logger.error("发生错误，未正常关闭，可能会丢失数据！");
                    }
                    executorService.shutdownNow();
                    try {
                        executorService.awaitTermination(2,TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                    }
                    try {
                        networkModule.stop();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    runState=false;
                    this.messageQueue.shutdown();
                }
            }
        }
    }
    private void saveMsgId(){
        FileOutputStream fos=null;
        DataOutputStream dos = null;
        File f = new File(System.getProperty("user.dir"), ".mqtt/" + connectConfig.getClientId() + "/data/id.data");
        if(connectConfig.isCleanSession()){
            f.delete();
            return;
        }
        try {
            if(!f.exists()||!f.isFile()){
                f.createNewFile();
                logger.info("msgId：{}",f.getAbsolutePath());
            }
            fos = new FileOutputStream(f);
            dos = new DataOutputStream(fos);
           dos.write(msgId);
           dos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void initMsgId(){
        FileInputStream fis = null;
        DataInputStream dis = null;
        File f = new File(System.getProperty("user.dir"), ".mqtt/" + connectConfig.getClientId() + "/data/id.data");
        if(f.exists()&&f.isFile()){
            try {
                fis = new FileInputStream(f);
                dis = new DataInputStream(fis);
                msgId=dis.readInt();
                logger.info("msgId:{}",msgId);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static Builder getBuilder(){
        return  new Builder();
    }
}
