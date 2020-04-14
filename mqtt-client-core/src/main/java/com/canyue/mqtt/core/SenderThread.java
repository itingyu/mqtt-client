package com.canyue.mqtt.core;

import com.canyue.mqtt.core.packet.BasePacket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SenderThread implements Runnable{
    private final ConcurrentLinkedQueue clq;
    private OutputStream os;
    public SenderThread(OutputStream os, ConcurrentLinkedQueue clq){
        this.os=os;
        this.clq=clq;
    }
    public void run() {
        Thread.currentThread().setName("SenderThread");
        try{
            while (true){
                BasePacket m=(BasePacket) clq.poll();
                if(m!=null){
                    send(os, m);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(OutputStream os, BasePacket msg) throws IOException {
        System.out.println("发送："+msg);
        if(os!=null){
            os.write(msg.getHeaders());
            os.write(msg.getPayload());
            os.flush();
        }else {
            System.out.println("socket 已经被关闭了");
        }
    }
}