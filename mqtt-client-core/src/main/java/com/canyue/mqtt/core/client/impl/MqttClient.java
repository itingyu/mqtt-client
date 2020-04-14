package com.canyue.mqtt.core.client.impl;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.ReceiverThread;
import com.canyue.mqtt.core.SenderThread;
import com.canyue.mqtt.core.network.INetworkModule;
import com.canyue.mqtt.core.network.impl.TcpModule;
import com.canyue.mqtt.core.packet.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MqttClient {
    ConcurrentLinkedQueue<BasePacket> clq;
    INetworkModule tcp;
    ScheduledExecutorService scheduledThreadPool;
    private static int msgId=1;
    public void start(){
        Thread.currentThread().setName("MainThread");
        clq = new ConcurrentLinkedQueue();
        tcp = new TcpModule("127.0.0.1",1883);
        scheduledThreadPool = Executors.newScheduledThreadPool(5);
        try {
            tcp.start();
            scheduledThreadPool.schedule(new SenderThread(tcp.getOutputStream(),clq),0, TimeUnit.SECONDS);
            scheduledThreadPool.schedule(new ReceiverThread(tcp.getInputStream(),clq),0,TimeUnit.SECONDS);
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
        PublishPacket publishMsg = new PublishPacket(msg);
        clq.offer(publishMsg);
    }
    public void unsubscribe(String[] topics) throws IOException {
        UnsubscribePacket mqttUnsubscribeMsg = new UnsubscribePacket(
               topics,
                msgId);
        clq.offer(mqttUnsubscribeMsg);
    }
    public void subscribe(String[] topics,int[] qosList) throws IOException {
        SubscribePacket mqttSubscribeMsg=new SubscribePacket(
                topics,
                qosList,msgId);
        msgId++;
        clq.offer(mqttSubscribeMsg);
    }

    public void disconnect() throws IOException {
        BasePacket disconnectMsg = new DisconnectPacket();
        clq.offer(disconnectMsg);
    }
    public void connect(String username,String password,String clientId,Message willMessage,int keepAlive,boolean cleanSession) throws IOException {
        BasePacket mqttConnectMsg = new ConnectPacket(clientId
                ,willMessage,username,password,cleanSession,keepAlive);
        //连接报文
        clq.offer(mqttConnectMsg);
    }
    public void ping() throws IOException {
        BasePacket pingReq = new PingReqPacket();
       clq.offer(pingReq);
    }
}
