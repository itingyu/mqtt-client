package com.canyue.mqtt.core.listener;

import com.canyue.mqtt.core.MessageReceivedObject;

import java.util.EventListener;

public interface MessageReceivedListener extends EventListener {
	
	void messageArrived(MessageReceivedObject messageReceivedObject);
}
