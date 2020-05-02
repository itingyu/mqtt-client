package com.canyue.mqtt.core.packet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author canyue
 * 心跳请求
 *      固定报头：
 *                      byte1：0xc0
 *              剩余长度byte2：0x00
 *
 */
public class PingReqPacket extends BasePacket{
	private final  PacketType type = PacketType.PINGREQ_TYPE;
	private static Logger logger= LoggerFactory.getLogger(PingReqPacket.class);
	public PingReqPacket(byte[] data) {
		super();
	}
	
	public PingReqPacket() {
		logger.debug("ping req 报文生成完毕!");
	}
	
	@Override
    public byte[] getVariableHeader() throws IOException {
        return new byte[0];
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
