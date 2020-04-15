package com.canyue.mqtt.core;

import com.canyue.mqtt.core.packet.BasePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SenderThread implements Runnable{
    private final ConcurrentLinkedQueue clq;
    private OutputStream os;
    private static Logger logger = LoggerFactory.getLogger(SenderThread.class);
    public SenderThread(OutputStream os, ConcurrentLinkedQueue clq){
        this.os=os;
        this.clq=clq;
    }
    public void run() {
        Thread.currentThread().setName("SenderThread");
        logger.debug("senderThread已启动!");
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
        if(os!=null){
            os.write(msg.getHeaders());
            os.write(msg.getPayload());
            os.flush();
            logger.info("{}报文发送成功",msg.getType());
        }else {
            logger.warn("{}报文发送失败!",msg.getType());
          logger.warn("socket 已经被关闭了!");
        }
    }
}