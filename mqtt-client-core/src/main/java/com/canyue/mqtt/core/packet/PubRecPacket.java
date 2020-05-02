package com.canyue.mqtt.core.packet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author canyue
 *发布收到：
 *      固定报头：
 *                    byte1:0x50
 *          剩余长度：byte2:0x02
 *       可变报头：
 *          报文标识符
 */

public class PubRecPacket extends BasePacket{
    
    private final  PacketType type = PacketType.PUBREC_TYPE;
    private static Logger logger = LoggerFactory.getLogger(PubRecPacket.class);

    public PubRecPacket(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        try {
            this.msgId = dis.readUnsignedShort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("publish rec报文解析完毕:" +
                "\tmsgId:{};",msgId);
    }
    public PubRecPacket(int msgId){
        this.msgId=msgId;
        logger.debug("publish rec报文生成完毕:" +
                "\tmsgId:{};",msgId);
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    private int msgId;

    @Override
    public byte[] getVariableHeader() throws IOException {
        return new byte[]{(byte)((msgId>>8)&0xff),(byte)((msgId>>0)&0xff)};
    }

    @Override
    public byte[] getPayload() throws IOException {
        return new byte[0];
    }

    @Override
    public byte getFixHeaderFlag() {
        return 0;
    }
    @Override
    public PacketType getType() {
        return type;
    }
}
