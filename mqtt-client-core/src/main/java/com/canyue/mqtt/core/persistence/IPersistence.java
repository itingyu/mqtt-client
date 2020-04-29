package com.canyue.mqtt.core.persistence;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.exception.MqttPersistenceException;
import com.canyue.mqtt.core.packet.BasePacket;

import java.util.ArrayList;
import java.util.List;

public interface IPersistence extends AutoCloseable {

    public void open(String clientId) throws MqttPersistenceException;

    public void close() throws MqttPersistenceException;

    public void clear() throws MqttPersistenceException;

    public Object find(String string) throws MqttPersistenceException;

    public List<BasePacket> getAllNeed2Retry() throws MqttPersistenceException;

    public void save(String string ,Object object) throws MqttPersistenceException;

    public void remove(String string) throws MqttPersistenceException;

}
