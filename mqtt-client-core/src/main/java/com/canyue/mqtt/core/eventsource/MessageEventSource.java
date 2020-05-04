package com.canyue.mqtt.core.eventsource;

import com.canyue.mqtt.core.event.MessageEvent;
import com.canyue.mqtt.core.listener.MessageReceivedListener;


/**
 * @author canyue
 */
public class MessageEventSource {

	private MessageReceivedListener listener ;

	public void setListener(MessageReceivedListener listener) {
		this.listener = listener;
	}
	public void notifyListenerEvent(MessageEvent messageEvent){
		this.listener.messageArrived(messageEvent);
	}
}
