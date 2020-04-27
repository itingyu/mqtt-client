package com.canyue.mqtt.core.persistence.impl;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.packet.BasePacket;
import com.canyue.mqtt.core.persistence.IPersistence;

import java.util.List;

//do nothing
public class DefaultPersistence implements IPersistence {

    @Override
    public void open(String clientId) throws Exception {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void clear() throws Exception {

    }

    @Override
    public Object find(String string) throws Exception {
        return null;
    }

    @Override
    public List<BasePacket> getAllNeed2Retry() throws Exception {
        return null;
    }

    @Override
    public void save(String string, Object object) throws Exception {

    }

    @Override
    public void remove(String string) throws Exception {

    }
}
