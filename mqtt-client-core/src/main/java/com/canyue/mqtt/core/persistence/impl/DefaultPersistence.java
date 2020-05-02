package com.canyue.mqtt.core.persistence.impl;

import com.canyue.mqtt.core.packet.BasePacket;
import com.canyue.mqtt.core.persistence.IPersistence;

import java.util.List;

/**
 * @author canyue
 */
public class DefaultPersistence implements IPersistence {

    @Override
    public void open(String clientId) {

    }

    @Override
    public void close() {

    }

    @Override
    public void clear() {

    }

    @Override
    public Object find(String string) {
        return null;
    }

    @Override
    public List<BasePacket> getAllNeed2Retry() {
        return null;
    }

    @Override
    public void save(String string, Object object) {

    }

    @Override
    public void remove(String string) {

    }
}
