package com.canyue.mqtt.core;

import com.canyue.mqtt.core.exception.MqttPersistenceException;
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
        while (true){
            try {
                basePacket = PacketUtils.acquirePacket(dis);
                messageQueue.handleReceivedMsg(basePacket);
            }catch (MqttPersistenceException e){
                logger.error("持久化消息时发生了异常",e);
                break;
            } catch (IOException e){
                logger.error("io异常，读取消息失败!");
                this.messageQueue.getClientCallback().shutdown();
                break;
            }
        }
        logger.info("ReceiverThread已停止！");
    }

}