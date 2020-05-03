package com.canyue.mqtt.core.packet;

import com.canyue.mqtt.core.util.PacketUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author canyue
 */
public abstract class BasePacket {

	/**
	 * 获取报文的可变头部
	 * @return
	 * @throws IOException
	 */
	public abstract byte[] getVariableHeader() throws IOException ;

	/**
	 * 获取报文的有效载荷
	 * @return
	 * @throws IOException
	 */
	public abstract byte[] getPayload() throws IOException;

	/**
	 * 获取报文的固定头部
	 * @return
	 */
	public abstract byte getFixHeaderFlag();

	/**
	 * 获取报文的类型
	 * @return
	 */
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
