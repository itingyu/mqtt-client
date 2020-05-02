package com.canyue.mqtt.core.listener;

import com.canyue.mqtt.core.eventobject.MessageEvent;
import java.util.EventListener;



/**
 * @author canyue
 */
public interface MessageReceivedListener extends EventListener {
	/**
	 * 接收到消息后调用
	 * @param messageEvent
	 */
	void messageArrived(MessageEvent messageEvent);
}
