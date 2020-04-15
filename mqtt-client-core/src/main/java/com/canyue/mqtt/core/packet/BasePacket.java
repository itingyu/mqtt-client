package com.canyue.mqtt.core.packet;

import com.canyue.mqtt.core.util.PacketUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class BasePacket {
	
	public abstract byte[] getVariableHeader() throws IOException ;
	
	public abstract byte[] getPayload() throws IOException;
	
	public abstract byte getFixHeaderFlag();
	
	public abstract PacketType getType();
	
	public byte[] getHeaders() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte byte1 = (byte) ((getFixHeaderFlag()&0x0f)^((getType().getValue()&0x0f)<<4));
		baos.write(byte1);
		byte[] variableHeader = getVariableHeader();
		byte[] len = PacketUtils.encodeRenmainingLength(variableHeader.length+getPayload().length);
		baos.write(len);
		baos.write(variableHeader);
		return baos.toByteArray();
	}
}
