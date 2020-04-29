package com.canyue.mqtt.core.packet;

import com.canyue.mqtt.core.util.PacketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 取消订阅：
 *      固定报头：
 *              byte1:0xa2
 *              剩余长度
 *      可变报头：
 *              报文标识符
 *       有效载荷：
 *              主题过滤器列表（至少包含一个）
 *                  主题过滤器: mqtt-utf8编码
 */
public class UnsubscribePacket extends BasePacket {
    private final static PacketType type = PacketType.UNSUBSCRIBE_TYPE;
    private String[] topicsFilters;
    
    public UnsubscribePacket(byte[] data) {
		super();
	}
    
    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    private int msgId;
    private static Logger logger= LoggerFactory.getLogger(UnsubscribePacket.class);
    public UnsubscribePacket(String[] topicsFilters,int msgId){
        if(topicsFilters==null){
            logger.warn("主题列表不合法!");
            throw new IllegalArgumentException("主题列表不合法!");
        }
        this.topicsFilters=topicsFilters;
        this.msgId = msgId;
        logger.debug("unsubscribe 报文生成完毕:" +
                "\tmsgId:{};",msgId);
    }
    public byte[] getVariableHeader() throws IOException {
        return new byte[]{(byte)((msgId>>8)&0xff),(byte)((msgId>>0)&0xff)};
    }
    public byte[] getPayload() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        int index = 0;
        while (index<topicsFilters.length){
            PacketUtils.encodeMQTTUTF8(dos,topicsFilters[index]);
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
}
