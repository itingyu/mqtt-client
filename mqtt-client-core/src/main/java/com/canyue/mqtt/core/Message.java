package com.canyue.mqtt.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author canyue
 * mqtt消息内容
 */
public class Message implements Serializable {
    private String name = "message";
    private int qos =0;
    private String topic;
    private byte[] payload =new byte[0];
    private boolean retain = false;
    private boolean dup = false;
    private int msgId;
    public Message(String topic){
        this.topic=topic;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public boolean isDup() {
        return dup;
    }

    public void setDup(boolean dup) {
        this.dup = dup;
    }

    public boolean isRetain() {
        return retain;
    }

    public void setRetain(boolean retain) {
        this.retain = retain;
    }

    public int getQos() {
        return qos;
    }
    private static Logger logger = LoggerFactory.getLogger(Message.class);
    public void setQos(int qos)  {
        this.qos = qos;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic)  {
        this.topic = topic;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] msg) {
        this.payload = msg;
    }
    
    
    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", qos:" + qos +
                ", topic:'" + topic + '\'' +
                ", payload:" + Arrays.toString(payload) +
                ", retain:" + retain +
                ", dup:" + dup +
                ", msgId:" + msgId +
                '}';
    }
}
