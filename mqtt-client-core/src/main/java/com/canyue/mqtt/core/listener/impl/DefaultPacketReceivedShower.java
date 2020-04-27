package com.canyue.mqtt.core.listener.impl;


import com.canyue.mqtt.core.PacketReceived;
import com.canyue.mqtt.core.listener.PacketReceivedListener;

public class DefaultPacketReceivedShower implements PacketReceivedListener {
    public void PacketArrived(PacketReceived messageReceivedObject) {
        System.out.println("默认消息处理:"+messageReceivedObject.getSource().toString());
    }
}
