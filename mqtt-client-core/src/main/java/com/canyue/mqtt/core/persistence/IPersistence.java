package com.canyue.mqtt.core.persistence;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.packet.BasePacket;

import java.util.ArrayList;
import java.util.List;

public interface IPersistence extends AutoCloseable {

    public void open(String clientId) throws Exception;

    public void close() throws Exception;

    public void clear() throws Exception;

    public Object find(String string) throws Exception;

    public List<BasePacket> getAllNeed2Retry() throws Exception;

    public void save(String string ,Object object) throws Exception;

    public void remove(String string) throws Exception;

}
