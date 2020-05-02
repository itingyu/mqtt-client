package com.canyue.mqtt.core.packet;

/**
 * @author canyue
 *
 * 断开连接报文
 *      固定报头：       byte1: 0xe0
 *              剩余长度 byte2  0x00
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class DisconnectPacket extends BasePacket{
	
	private final  PacketType type = PacketType.DISCONNECT_TYPE;
	private static Logger logger= LoggerFactory.getLogger(DisconnectPacket.class);
	public DisconnectPacket(byte[] data) {
		super();
	}
	
	public DisconnectPacket() {
		logger.debug("disconnect报文已生成!");
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
