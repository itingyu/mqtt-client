package com.canyue.mqtt.core;

import com.canyue.mqtt.core.packet.BasePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;


/**
 * @author canyue
 */
public class SenderThread implements Runnable{
    private  MessageQueue messageQueue;

    public void setPingLock(Object pingLock) {
        this.pingLock = pingLock;
    }

    private Object pingLock;
    private OutputStream os;
    private static Logger logger = LoggerFactory.getLogger(SenderThread.class);
    public SenderThread(OutputStream os){
        this.os=os;
    }

    public void setMessageQueue(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("SenderThread");
        logger.debug("senderThread已启动!");
        try{
            while (true){
                BasePacket m=(BasePacket) messageQueue.getSend();
                if(m!=null){
                    send(os, m);
                }
            }
        } catch (IOException e) {
            logger.error("io异常，发送失败！");
        } catch (InterruptedException e) {
            logger.error("SenderThread被中断！");
        }
        this.messageQueue.getClientCallback().shutdown();
        logger.debug("SenderThread已停止！");
    }

    public void send(OutputStream os, BasePacket msg) throws IOException {
        if(os!=null){
            synchronized (pingLock){
                os.write(msg.getHeaders());
                os.write(msg.getPayload());
                os.flush();
                pingLock.notify();
            }
            logger.info("{}报文发送成功",msg.getType());

        }else {
            logger.warn("{}报文发送失败!",msg.getType());
          logger.warn("socket 已经被关闭了!");
        }
    }
}