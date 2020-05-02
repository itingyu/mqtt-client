package com.canyue.mqtt.core.packet;

import com.canyue.mqtt.core.util.PacketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author canyue
 * 订阅主题：
 *      固定报头：
 *              byte1 :0x82
 *              剩余长度：可变报头长度（2字节）+有效载荷长度
 *      可变报头：
 *          报文标识符（2字节）：非零16位，重发必须使用相同的报文标识符，
 *      有效载荷：
 *          主题过滤器列表：（至少包含一对）
 *                  主题过滤器                 : MQTT-utf8编码
 *                  服务质量要求（1字节）   : 没有用到高六位。如果Qos不等于0,1,2，则为不合法.
 */
public class SubscribePacket extends BasePacket {
    private final  PacketType type = PacketType.SUBSCRIBE_TYPE;
    private int topicCount=0;
    private String[] topicsFilters;
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
    private static Logger logger= LoggerFactory.getLogger(SubscribePacket.class);
    public SubscribePacket(String[] topicsFilters,int[] requiredQos,int msgId){
        if(topicsFilters==null||requiredQos==null||topicsFilters.length!=requiredQos.length){
            logger.warn("主题过滤器列表不合法!");
            throw new IllegalArgumentException("主题过滤器列表不合法!");
        }
        this.msgId=msgId;
        this.topicsFilters=topicsFilters;
        topicCount=topicsFilters.length;
        this.requiredQos=requiredQos;
        logger.debug("subscribe报文生成完毕:" +
                "\tmsgId:{}," +
                "\ttopicsFilters:{}," +
                "\trequiredQos:{};",msgId,Arrays.toString(topicsFilters),Arrays.toString(requiredQos));
    }

    @Override
    public byte[] getVariableHeader() throws IOException {
        return new byte[]{(byte)((msgId>>8)&0xff),(byte)((msgId>>0)&0xff)};
    }

    @Override
    public byte[] getPayload() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        int index = 0;
        while (index<topicsFilters.length){
            PacketUtils.encodeMqttUtf8(dos,topicsFilters[index]);
            dos.writeByte(requiredQos[index]);
            index++;
        }
        return baos.toByteArray();
    }

    @Override
    public byte getFixHeaderFlag() {
        return 2;
    }
    @Override
    public PacketType getType() {
        return type;
    }
}
