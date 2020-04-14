package com.canyue.mqtt.core.packet;

import java.io.IOException;

/**
 * 心跳请求
 *      固定报头：
 *                      byte1：0xc0
 *              剩余长度byte2：0x00
 *
 */
public class PingReqPacket extends BasePacket{
	private final static PacketType type = PacketType.PINGREQ_TYPE;
	
	public PingReqPacket(byte[] data) {
		super();
	}
	
	public PingReqPacket() {
	
	}
	
	public byte[] getVariableHeader() throws IOException {
        return new byte[0];
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
    @Override
    public String toString() {
        return "PingReqPacket{" +
                "type=" + type +
                '}';
    }
}
