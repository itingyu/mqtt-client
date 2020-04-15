package com.canyue.mqtt.core.packet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;

/**
 *订阅确认：
 *    固定报头：
 *          byte1:0x90
 *          剩余长度
 *    可变报头：
 *          报文标识符（2字节）
 *     有效载荷：
 *          返回码清单（与主题过滤器一一对应）
 *                返回码字段（1字节）:
 *                              0x00 ：最大Qos 0
 *                              0x01 ：最大Qos 1
 *                              Ox02 : 最大Qos 2
 *                              0x02 : 失败
 *                              其他返回码是保留的，不能使用
 */
public class SubAckPacket extends BasePacket {

    private int[] returnCodes;
    private final static PacketType type=PacketType.SUBACK_TYPE;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    private int msgId;
    private static Logger logger= LoggerFactory.getLogger(SubAckPacket.class);
    public SubAckPacket(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        try {
            this.msgId = dis.readUnsignedShort();
            this.returnCodes = new int[data.length-2];
            int index=0;
            int qos=-1;
            while ((qos=dis.read())!=-1){
                returnCodes[index]=qos;
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("subscribe ack报文解析完毕:" +
                "\tmsgId:{}," +
                "\treturnCodes:{};",msgId,Arrays.toString(returnCodes));
    }

    public byte[] getVariableHeader() throws IOException {
        return new byte[]{(byte)((msgId>>8)&0xff),(byte)((msgId>>0)&0xff)};
    }

    public byte[] getPayload() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        int index=0;
        while (index<returnCodes.length){
            dos.writeByte(returnCodes[index]);
            index++;
        }
        return baos.toByteArray();
    }

    public byte getFixHeaderFlag() {
        return 0;
    }
    public PacketType getType() {
        return type;
    }
}
