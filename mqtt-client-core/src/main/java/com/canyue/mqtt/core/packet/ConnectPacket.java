package com.canyue.mqtt.core.packet;


import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.util.PacketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * @author canyue
 *
 * 连接报文：
 *      byte1 :  0x10
 *      剩余长度
 *      可变报头：(10字节)
 *          协议名：PROTOCOL_NAME:MQTT     (编码后2+4个字节)
 *          协议级别：PROTOCOL_LEVEL:4      (1个字节）
 *          连接标志：CONNECT_FLAGS:       (1个字节)
 *                      0   保留位：Reserved              必须为0
 *                      1   清理会话：Clean Session
 *                      2   遗嘱标志：Will Flag
 *                      3,4 遗嘱Qos：Will Qos              第3位和第4位  不能等于3
 *                      5   遗嘱保留：Will Retain
 *                      6   用户名标志：User Name Flag
 *                      7   密码标志：Password Flag
 *          保持连接：Keep Alive       MSB LSB （2字节）   单位秒         0，关闭保持连接功能
 *                      在客户端传输完成一个控制报文时刻到发送下一个报文时刻，
 *      有效载荷:字段由连接标志决定
 *             客户端标识符：  （可以为0字节，清理会话必须设置为1）
 *             遗嘱主题：Will Topic
 *             遗嘱消息：Will Message   由遗嘱标志决定
 *             用户名：User Name        mqtt的utf8
 *             密码：Password
 *
 *
 */
public class ConnectPacket extends BasePacket {
    private final  PacketType type = PacketType.CONNECT_TYPE;
    private String clientId;
    /**
     * 遗嘱消息
     */
    private Message willMessage;
    private String userName;
    private String password;
    private boolean cleanSession;
    private int keepAlive = 0;



    //这个构造函数用不到，因为是客户端
    public ConnectPacket(byte[] data) {
    
    }
    private static Logger logger = LoggerFactory.getLogger(ConnectPacket.class);
    //用于根据客户端的需求生成相应的connect报文
    public ConnectPacket(String clientId, Message willMessage, String userName, String password, boolean cleanSession, int keepAlive){

        this.cleanSession = cleanSession;
        this.clientId=clientId;
        this.keepAlive = keepAlive;
        this.userName=userName;
        this.password=password;
        this.willMessage=willMessage;
        logger.debug("connect报文生成完毕：" +
                "\tcleanSession:{}," +
                "\tclientId:{}," +
                "\tkeepAlive:{}," +
                "\tusername:{}," +
                "\tpassword:{}," +
                "\twillMessage:{};",cleanSession,clientId,keepAlive,userName,password,willMessage);
    }


    @Override
    public byte[] getVariableHeader() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte connectFlags = 0;
        PacketUtils.encodeMqttUtf8(dos,"MQTT");
        dos.writeByte(4);
        if(cleanSession==true){
            connectFlags|=0x02;
        }
        if(willMessage!=null){
            connectFlags|=0x04;
            connectFlags|=willMessage.getQos()<<3;
            if(willMessage.isRetain()) {
                connectFlags|=0x20;
            }
        }
        if(userName!=null){
            connectFlags|=0x80;
            if(password!=null&&password.length()>0){
                connectFlags|=0x40;
            }
        }
        dos.writeByte(connectFlags);
        dos.writeShort(keepAlive);
        dos.flush();
        return baos.toByteArray();
    }

    @Override
    public byte[] getPayload() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        PacketUtils.encodeMqttUtf8(dos,clientId);
        if(willMessage!=null){
            PacketUtils.encodeMqttUtf8(dos,willMessage.getTopic());
            dos.writeShort(willMessage.getPayload().length);
            dos.write(willMessage.getPayload());
        }
        if (userName!=null){
            PacketUtils.encodeMqttUtf8(dos, userName);
            if (password != null&&password.length()>0) {
                PacketUtils.encodeMqttUtf8(dos, password);
            }
        }
        dos.flush();
        return baos.toByteArray();

    }
    @Override
    public PacketType getType() {
        return type;
    }
    @Override
    public byte getFixHeaderFlag() {
        return 0;
    }
}
