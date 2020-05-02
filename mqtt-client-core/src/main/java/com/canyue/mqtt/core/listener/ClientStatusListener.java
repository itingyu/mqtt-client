package com.canyue.mqtt.core.listener;

import com.canyue.mqtt.core.eventobject.ClientStatusEvent;

import java.util.EventListener;


/**
 * @author canyue
 */
public interface ClientStatusListener extends EventListener {
    /**
     * 连接完成后调用
     * @param clientStatusEvent
     */
    public void connectCompeted(ClientStatusEvent clientStatusEvent);

    /**
     * 连接断开后调用
     * @param clientStatusEvent
     */
    public void shutdown(ClientStatusEvent clientStatusEvent);
}
