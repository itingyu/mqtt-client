package com.canyue.mqtt.core.utils;

import com.canyue.mqtt.core.packet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author canyue
 */
public class PacketUtils {
	public static final int MAX_REMAINING_LENGTH = 268435455;
	private PacketUtils() {
	
	}
	/**
	 * 从输入流里读取报文，主要是接收时调用
	 * @param is
	 * @return
	 */
	private static Logger logger  = LoggerFactory.getLogger(PacketUtils.class);
	public static BasePacket acquirePacket(InputStream is) throws IOException {
		DataInputStream dis = new DataInputStream(is);
		int first = dis.readUnsignedByte();
		//固定头部
		byte type  = (byte) (first>>4);
		byte flag = (byte) (first&0x0f);
		int remLen = decodeRenmainingLength(dis);
		//可变报头和有效负载，由子类解析
		byte[] data = new byte[0];
		if(remLen>0){
			data = new byte[remLen];
			dis.readFully(data,0,remLen);
		}
		switch (type){
			case 1:return new ConnectPacket(data);
			case 2:return new ConnectAckPacket(data);
			//publish报文需要固定报文flag
			case 3:return new PublishPacket(flag,data);
			case 4:return new PubAckPacket(data);
			case 5:return new PubRecPacket(data);
			case 6:return new PubRelPacket(data);
			case 7:return new PubCompPacket(data);
			case 8:return new SubscribePacket(data);
			case 9:return new SubAckPacket(data);
			case 10:return new UnsubscribePacket(data);
			case 11:return new UnsubAckPacket(data);
			case 12:return new PingReqPacket(data);
			case 13:return new PingRespPacket(data);
			case 14:return new DisconnectPacket(data);
			default:logger.debug("broker发来未知类型消息!");
				throw new IllegalStateException("broker发来未知类型消息!");
		}
	}
	
	
	public static byte[] encodeRenmainingLength(int remLen){
		if(remLen>MAX_REMAINING_LENGTH||remLen<0){
			logger.debug("非法的剩余长度!");
			throw new IllegalArgumentException("非法的剩余长度!");
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//记录字节数
		int count=0;
		do{
			byte data = (byte)(remLen%128);
			remLen/=128;
			if(remLen>0){
				//后面还有字节，最高位置1
				data|=0x80;
			}
			baos.write(data);
			count++;
			//规定了最多四个字节
		}while((remLen>0)&&(count<4));
		return baos.toByteArray();
	}
	/**
	 * 解码最大长度，要从流里面读出来之，才能解码，
	 * @param in
	 * @return
	 * @exception IOException
	 */
	public static int decodeRenmainingLength(DataInputStream in) throws IOException {
		int remLen=0;
		byte data=0;
		int base=1;
		do{
			data= in.readByte();
			//data低七位是高位数据
			remLen+=(data&0x7f)*base;
			base*=128;
			//最高位不为0，表示还有字节
		}while((data&0x80)!=0);
		
		if(remLen>MAX_REMAINING_LENGTH||remLen<0){
			logger.debug("broker发来不合法长度的报文!");
			throw new IllegalStateException("broker发来不合法长度的报文!");
		}
		return remLen;
	}
	
	
	/**
	 * 使用mqtt协议规定的utf-8格式
	 *          长度(2字节，eg:5  :MSB,0x00 LSB,0x05)+utf-8字符串，
	 *          编码主要用于写入流的时候调用
	 * @param dos
	 * @param rawString
	 * @throws IOException
	 */
	public static void encodeMqttUtf8(DataOutputStream dos, String rawString) throws IOException {
		byte[] rawStringBytes = rawString.getBytes("UTF8");
		int rawStringLength = rawStringBytes.length;
		// 9-16位
		dos.write((byte)((rawStringLength>>>8)&0xff));
		//0-8位
		dos.write((byte)((rawStringLength>>>0)&0xff));
		dos.write(rawStringBytes);
	}
	/**
	 * 解码，用于从流中读取字符串时调用
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	public static String decodeMQTTUTF8(DataInputStream dis) throws IOException {
		//长度占两字节,非负
		int length = dis.readUnsignedShort();
		byte[] bytes=new byte[length];
		dis.readFully(bytes,0,length);
		return new String(bytes,"UTF8");
	}
}
