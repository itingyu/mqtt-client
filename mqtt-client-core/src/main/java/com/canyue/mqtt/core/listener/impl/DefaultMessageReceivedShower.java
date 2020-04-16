package com.canyue.mqtt.core.listener.impl;

import com.canyue.mqtt.core.MessageReceivedObject;
import com.canyue.mqtt.core.listener.MessageReceivedListener;

public class DefaultMessageReceivedShower implements MessageReceivedListener {
    public void messageArrived(MessageReceivedObject messageReceivedObject) {
        System.out.println("默认消息处理:"+messageReceivedObject.getSource().toString());
    }
}
