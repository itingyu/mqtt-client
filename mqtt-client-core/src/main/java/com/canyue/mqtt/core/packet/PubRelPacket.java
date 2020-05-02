package com.canyue.mqtt.core.packet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author canyue
 * 发布释放：
 *      固定报头：
 *              byte1:0x62
 *              byte2:0x02
 *      可变报头：
 *              报文标识符
 */

public class PubRelPacket extends BasePacket {
    
    private final  PacketType type = PacketType.PUBREL_TYPE;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    private int msgId;
    private static Logger logger= LoggerFactory.getLogger(PubRelPacket.class);
    public PubRelPacket(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        try {
            this.msgId = dis.readUnsignedShort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("publish rel报文解析完毕:" +
                "\tmsgId:{};",msgId);
    }
    public PubRelPacket(int msgId){
        this.msgId=msgId;
        logger.debug("publish rel报文生成完毕:" +
                "\tmsgId:{};",msgId);
    }

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
        return 2;
    }
    @Override
    public PacketType getType() {
        return type;
    }
}
