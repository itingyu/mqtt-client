package com.canyue.mqtt.core.listener;

import com.canyue.mqtt.core.PacketReceived;
import java.util.EventListener;

public interface PacketReceivedListener extends EventListener {
	
	void PacketArrived(PacketReceived messageReceivedObject);
}
