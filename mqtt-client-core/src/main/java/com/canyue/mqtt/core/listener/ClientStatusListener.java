package com.canyue.mqtt.core.listener;

import com.canyue.mqtt.core.event_object.ClientStatusEvent;

import java.util.EventListener;

public interface ClientStatusListener extends EventListener {
    public void connectCompeted(ClientStatusEvent clientStatusEvent);
    public void shutdown(ClientStatusEvent clientStatusEvent);
}
