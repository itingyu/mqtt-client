package com.canyue.mqtt.core;

import com.canyue.mqtt.core.packet.BasePacket;

import java.util.EventObject;

public class PacketReceived extends EventObject {
	/**
	 * Constructs a prototypical Event.
	 *
	 * @param basePacket The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public PacketReceived(BasePacket basePacket) {
		super(basePacket);
	}
	public void printSource(){
		System.out.println(source);
	}
}
