package com.canyue.mqtt.core.packet;

import java.io.IOException;

public class PingRespPacket extends BasePacket {
	
	private final static PacketType type = PacketType.PINGRESP_TYPE;
	
	public PingRespPacket(byte[] data) {
		super();
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
        return "PingRespPacket{" +
                "type=" + type +
                '}';
    }
}
