package com.canyue.mqtt.core.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * 确认连接请求
 *      固定报头：
 *                        byte1：0x20
 *              可变长度：byte2: 0x02
 *      可变报头：
 *              连接确认标志：位7-1必须设置为0，第0(SP)位是当前会话标志(Session Present)
 *                      必须使服务端和客户端是否已存储的会话状态一致
 *                      如果客户端收到的与预期的不一致，客户端可以选择断开或继续
 *                      客户端丢弃会话方法：断开连接，cleanSession=1连接，再断开
 *              连接返回码：一个字节的无符号值
 *                      0x00 连接已接受
 *                      0x01 服务端不支持客户端请求的协议等级
 *                      0x02 不合格的客户端标识符
 *                      0x03 连接已建立，但MQTT服务不可用
 *                      0x04 无效的用户名和密码
 *                      0x05  客户端未被授权连接到此服务器
 *                      0x06-0xff 保留
 */
public class ConnectAckPacket extends BasePacket{
    private final static PacketType type = PacketType.CONNACK_TYPE;
    private byte sessionPresent;
    private int returnCode;

    public byte getSessionPresent() {
        return sessionPresent;
    }

    public int getReturnCode() {
        return returnCode;
    }

    //连接确认报文  固定报头+variableHeader
    public ConnectAckPacket(byte[] variableHeader) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(variableHeader);
        DataInputStream dis = new DataInputStream(bais);
        this.sessionPresent = dis.readByte();
        this.returnCode = dis.readUnsignedByte();
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
        return "ConnectAckPacket{" +
                "type=" + type +
                ", sessionPresent=" + sessionPresent +
                ", returnCode=" + returnCode +
                '}';
    }
}