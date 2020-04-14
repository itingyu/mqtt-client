package com.canyue.mqtt.core;


import java.io.Serializable;

public class Message implements Serializable {
    private String name = "message";
    private int qos =0;
    private String topic;
    private byte[] payload =new byte[0];
    private boolean retain = false;
    private boolean dup = false;
    private int msgId;
    public Message(String topic){
        this.topic = topic;
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

    public void setQos(int qos) {
        if(qos>2||qos<0){
            throw new IllegalArgumentException("不合法的qos质量");
        }
        this.qos = qos;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
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
        return "[name="+name +
                "{qos=" + qos +
                ", topic='" + topic + '\'' +
                ", payload=" +'\'' +new String(payload) +'\''+
                ", retain=" + retain +
                (getQos()>0? (",msgId="+msgId):"")+
                '}'+']';
    }
}