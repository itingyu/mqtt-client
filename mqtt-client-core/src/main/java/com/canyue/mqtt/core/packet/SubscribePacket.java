package com.canyue.mqtt.core.packet;

import com.canyue.mqtt.core.PacketParser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * 订阅主题：
 *      固定报头：
 *              byte1 :0x82
 *              剩余长度：可变报头长度（2字节）+有效载荷长度
 *      可变报头：
 *          报文标识符（2字节）：非零16位，重发必须使用相同的报文标识符，
 *      有效载荷：
 *          主题过滤器列表：（至少包含一对）
 *                  主题名                 : MQTT-utf8编码
 *                  服务质量要求（1字节）   : 没有用到高六位。如果Qos不等于0,1,2，则为不合法.
 */
public class SubscribePacket extends BasePacket {
    private final static PacketType type = PacketType.SUBSCRIBE_TYPE;
    private int topicCount=0;
    private String[] topics;
    private int[] requiredQos;
    
    public SubscribePacket(byte[] data) {
        super();
    }
    
    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    private int msgId;
    
    public SubscribePacket(String[] topics,int[] requiredQos,int msgId){
        if(topics==null||requiredQos==null||topics.length!=requiredQos.length){
            System.out.println("主题列表不合法");
            throw new IllegalArgumentException("主题列表不合法");
        }
        this.msgId=msgId;
        this.topics=topics;
        topicCount=topics.length;
        this.requiredQos=requiredQos;
    }

    public byte[] getVariableHeader() throws IOException {
        return new byte[]{(byte)((msgId>>8)&0xff),(byte)((msgId>>0)&0xff)};
    }

    public byte[] getPayload() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        int index = 0;
        while (index<topics.length){
            PacketParser.encodeMQTTUTF8(dos,topics[index]);
            dos.writeByte(requiredQos[index]);
            index++;
        }
        return baos.toByteArray();
    }

    public byte getFixHeaderFlag() {
        return 2;
    }
    public PacketType getType() {
        return type;
    }
    @Override
    public String toString() {
        return "SubscribePacket{" +
                "type=" + type +
                ", topicCount=" + topicCount +
                ", topics=" + Arrays.toString(topics) +
                ", requiredQos=" + Arrays.toString(requiredQos) +
                ", msgId=" + msgId +
                '}';
    }
}
