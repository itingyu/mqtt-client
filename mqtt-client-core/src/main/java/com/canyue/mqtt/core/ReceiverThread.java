package com.canyue.mqtt.core;

import com.canyue.mqtt.core.packet.*;
import com.canyue.mqtt.core.util.PacketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReceiverThread implements Runnable{
  
    private  MessageQueue messageQueue;
    private InputStream is;

    private static Logger logger = LoggerFactory.getLogger(ReceiverThread.class);
    public ReceiverThread(InputStream is){
        this.is = is;
    }

    public void setMessageQueue(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void run() {
        logger.info("receiverThread已启动!");
        DataInputStream dis = new DataInputStream(is);
        BasePacket basePacket= null;
        Thread.currentThread().setName("ReceiverThread");
        try {
            while (true){
                basePacket = PacketUtils.acquirePacket(dis);
                messageQueue.handleReceivedMsg(basePacket);
            }
        } catch (IOException e){
            logger.error("io异常，读取消息失败!");
        } catch (InterruptedException e) {
           logger.error("ReceiverThread被中断！");
        }catch (Exception e){
            logger.error("ReceiverThread发生异常");
        }
        logger.info("ReceiverThread已停止！");
    }

}