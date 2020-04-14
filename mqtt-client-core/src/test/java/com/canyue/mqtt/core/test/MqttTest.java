package com.canyue.mqtt.core.test;

import com.canyue.mqtt.core.ReceiverThread;
import com.canyue.mqtt.core.SenderThread;
import com.canyue.mqtt.core.network.INetworkModule;
import com.canyue.mqtt.core.network.impl.TcpModule;
import com.canyue.mqtt.core.packet.BasePacket;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MqttTest {
    public static void main(String[] args){
        Thread.currentThread().setName("MainThread");
        ConcurrentLinkedQueue<BasePacket> clq = new ConcurrentLinkedQueue();
        INetworkModule tcp = new TcpModule("127.0.0.1",1883);
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        try {
            tcp.start();
            scheduledThreadPool.schedule(new SenderThread(tcp.getOutputStream(),clq),0,TimeUnit.SECONDS);
            scheduledThreadPool.schedule(new ReceiverThread(tcp.getInputStream(),clq),0,TimeUnit.SECONDS);
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
}


