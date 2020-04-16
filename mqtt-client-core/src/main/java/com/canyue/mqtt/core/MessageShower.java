package com.canyue.mqtt.core;

import com.canyue.mqtt.core.listener.MessageReceivedListener;
import com.canyue.mqtt.core.listener.impl.DefaultMessageReceivedShower;

import java.util.ArrayList;
import java.util.List;

public class MessageShower {
	//private List<MessageReceivedListener> listeners = new ArrayList<MessageReceivedListener>();

//	public void addListener(MessageReceivedListener messageReceivedListener){
//		listeners.add(messageReceivedListener);
//	}
//	public void removeListener(MessageReceivedListener messageReceivedListener)
//	{
//		listeners.remove(messageReceivedListener);
//	}
//	public void notifyListenerEvent(MessageReceivedObject messageReceivedObject){
//		for (MessageReceivedListener listener : listeners) {
//			listener.messageArrived(messageReceivedObject);
//		}
//	}
	private MessageReceivedListener listener = new DefaultMessageReceivedShower();

	public void setListener(MessageReceivedListener listener) {
		this.listener = listener;
	}
	public void notifyListenerEvent(MessageReceivedObject messageReceivedObject){
		this.listener.messageArrived(messageReceivedObject);
	}
}
