package com.canyue.mqtt.core;

import java.util.EventObject;

public class MessageReceivedObject extends EventObject {
	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public MessageReceivedObject(Message source) {
		super(source);
	}
	public void printSource(){
		System.out.println(source);
	}
}
