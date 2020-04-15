package com.canyue.mqtt.core.packet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * 发布确认
 *      固定报头：
 *              byte1：0x40
 *              byte2：0x02
 *      可变报头：
 *              报文标识符
 */

public class PubAckPacket extends BasePacket{
    
    private final static PacketType type = PacketType.PUBACK_TYPE;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    private int msgId;
    private static Logger logger = LoggerFactory.getLogger(PubAckPacket.class);
    public PubAckPacket(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        try {
            this.msgId = dis.readUnsignedShort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("publish ack报文解析完毕:" +
                "\tmsgId:{};",msgId);
    }
    public PubAckPacket(int msgId){
        this.msgId=msgId;
    }
    public byte[] getVariableHeader() throws IOException {
        return new byte[]{(byte)((msgId>>8)&0xff),(byte)((msgId>>0)&0xff)};
    }

    public byte[] getPayload() throws IOException {
        return new byte[0];
    }

    public byte getFixHeaderFlag() {
        return 0;
    }
    public PacketType getType() {
        return type;
    }
}
