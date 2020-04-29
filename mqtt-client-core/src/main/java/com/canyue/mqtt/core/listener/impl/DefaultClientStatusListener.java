package com.canyue.mqtt.core.listener.impl;

import com.canyue.mqtt.core.event_object.ClientStatusEvent;
import com.canyue.mqtt.core.listener.ClientStatusListener;

public class DefaultClientStatusListener implements ClientStatusListener {
    @Override
    public void connectCompeted(ClientStatusEvent clientStatusEvent) {
    }

    @Override
    public void shutdown(ClientStatusEvent clientStatusEvent) {

    }
}
