package com.canyue.mqtt.core;

import com.canyue.mqtt.core.listener.PacketReceivedListener;
import com.canyue.mqtt.core.listener.impl.DefaultPacketReceivedShower;


public class MessageShower {

	private PacketReceivedListener listener = new DefaultPacketReceivedShower();

	public void setListener(PacketReceivedListener listener) {
		this.listener = listener;
	}
	public void notifyListenerEvent(PacketReceived packetReceived){
		this.listener.PacketArrived(packetReceived);
	}
}
