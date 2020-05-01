package com.canyue.mqtt.core.EventSource;

import com.canyue.mqtt.core.event_object.MessageEvent;
import com.canyue.mqtt.core.listener.MessageReceivedListener;

public class MessageEventSource {

	private MessageReceivedListener listener ;

	public void setListener(MessageReceivedListener listener) {
		this.listener = listener;
	}
	public void notifyListenerEvent(MessageEvent messageEvent){
		this.listener.MessageArrived(messageEvent);
	}
}
