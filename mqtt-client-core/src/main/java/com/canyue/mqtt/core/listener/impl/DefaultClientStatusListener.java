package com.canyue.mqtt.core.listener.impl;

import com.canyue.mqtt.core.eventobject.ClientStatusEvent;
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
}
