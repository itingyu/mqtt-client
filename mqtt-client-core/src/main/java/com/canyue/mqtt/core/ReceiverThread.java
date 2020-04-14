package com.canyue.mqtt.core;

import com.canyue.mqtt.core.packet.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ReceiverThread implements Runnable{
    IReciveMsgCallBack callBack = new ReciveMsgCallBackImpl();
    private final ConcurrentLinkedQueue clq;
    private InputStream is;
    private BasePacket basePacket;
    
    public ReceiverThread(InputStream is, ConcurrentLinkedQueue clq){
        this.is = is;
        this.clq=clq;
    }

    public void run() {
        DataInputStream dis = new DataInputStream(is);
        BasePacket basePacket= null;
        Thread.currentThread().setName("ReceiverThread");
        while (true){
            try {
                basePacket = PacketParser.acquirePacket(dis);
                handleMsg(basePacket,clq);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void handleMsg(BasePacket basePacket, ConcurrentLinkedQueue clq) {
        if(basePacket instanceof PublishPacket){
            Message msg = ((PublishPacket) basePacket).getMessage();
            callBack.messageArrived(msg);
            switch (msg.getQos()){
                case 1:clq.offer(new PubAckPacket(msg.getMsgId()));
                case 2:clq.offer(new PubRecPacket(msg.getMsgId()));
                default:break;
            }
        }else if(basePacket instanceof PubRecPacket){
            clq.offer(new PubRelPacket(((PubRecPacket)basePacket).getMsgId()));
        }else if(basePacket instanceof PubRelPacket){
            clq.offer(new PubCompPacket(((PubRelPacket)basePacket).getMsgId()));
        }
        //System.out.println("收到："+basePacket);
    }
}