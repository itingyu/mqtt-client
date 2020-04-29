package com.canyue.mqtt.core.listener;

import com.canyue.mqtt.core.event_object.MessageEvent;
import java.util.EventListener;

public interface MessageReceivedListener extends EventListener {
	
	void MessageArrived(MessageEvent messageEvent);
}
