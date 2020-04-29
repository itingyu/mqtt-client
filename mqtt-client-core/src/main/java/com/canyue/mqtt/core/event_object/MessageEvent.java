package com.canyue.mqtt.core.event_object;

import com.canyue.mqtt.core.Message;

import java.util.EventObject;

public class MessageEvent extends EventObject {

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	private Message message;
	public MessageEvent(Object source,Message message) {
		super(source);
		this.message=message;
	}

	public Message getMessage() {
		return message;
	}
}
