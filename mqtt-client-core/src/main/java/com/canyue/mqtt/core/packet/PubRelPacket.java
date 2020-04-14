package com.canyue.mqtt.core.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * 发布释放：
 *      固定报头：
 *              byte1:0x62
 *              byte2:0x02
 *      可变报头：
 *              报文标识符
 */

public class PubRelPacket extends BasePacket {
    
    private final static PacketType type = PacketType.PUBREL_TYPE;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    private int msgId;
    public PubRelPacket(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        try {
            this.msgId = dis.readUnsignedShort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public PubRelPacket(int msgId){
        this.msgId=msgId;
    }

    public byte[] getVariableHeader() throws IOException {
        return new byte[]{(byte)((msgId>>8)&0xff),(byte)((msgId>>0)&0xff)};
    }

    public byte[] getPayload() throws IOException {
        return new byte[0];
    }

    public byte getFixHeaderFlag() {
        return 2;
    }
    public PacketType getType() {
        return type;
    }
    @Override
    public String toString() {
        return "PubRelPacket{" +
                "type=" + type +
                ", msgId=" + msgId +
                '}';
    }
}
