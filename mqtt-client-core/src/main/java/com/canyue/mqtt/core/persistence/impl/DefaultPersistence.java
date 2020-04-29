package com.canyue.mqtt.core.persistence.impl;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.exception.MqttPersistenceException;
import com.canyue.mqtt.core.packet.BasePacket;
import com.canyue.mqtt.core.persistence.IPersistence;

import java.util.List;

//do nothing
public class DefaultPersistence implements IPersistence {

    @Override
    public void open(String clientId) throws MqttPersistenceException {

    }

    @Override
    public void close() throws MqttPersistenceException {

    }

    @Override
    public void clear() throws MqttPersistenceException {

    }

    @Override
    public Object find(String string) throws MqttPersistenceException {
        return null;
    }

    @Override
    public List<BasePacket> getAllNeed2Retry() throws MqttPersistenceException {
        return null;
    }

    @Override
    public void save(String string, Object object) throws MqttPersistenceException {

    }

    @Override
    public void remove(String string) throws MqttPersistenceException {

    }
}
