package com.canyue.mqtt.core.listener.impl;

import com.canyue.mqtt.core.event.ClientStatusEvent;
import com.canyue.mqtt.core.listener.ClientStatusListener;

/**
 * @author canyue
 */
public class DefaultClientStatusListener implements ClientStatusListener {
    @Override
    public void connectCompeted(ClientStatusEvent clientStatusEvent) {
    }

    @Override
    public void shutdown(ClientStatusEvent clientStatusEvent) {

    }

    @Override
    public void subscribeCompeted(ClientStatusEvent clientStatusEvent) {

    }

    @Override
    public void unsubscribeCompeted(ClientStatusEvent clientStatusEvent) {

    }
}
