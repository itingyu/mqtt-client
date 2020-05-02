package com.canyue.mqtt.core.persistence;


import com.canyue.mqtt.core.exception.MqttPersistenceException;
import com.canyue.mqtt.core.packet.BasePacket;

import java.util.List;


/**
 * @author canyue
 */
public interface IPersistence extends AutoCloseable {

    /**
     * 打开持久化工具
     * @param clientId
     * @throws MqttPersistenceException
     */
    public void open(String clientId) throws MqttPersistenceException;

    /**
     * 关闭持久化工具
     * @throws MqttPersistenceException
     */
    @Override
    public void close() throws MqttPersistenceException;

    /**
     * 清楚保存的数据
     * @throws MqttPersistenceException
     */
    public void clear() throws MqttPersistenceException;

    /**
     * 根据key获取的对应的对象
     * @param key
     * @return
     * @throws MqttPersistenceException
     */
    public Object find(String key) throws MqttPersistenceException;

    /**
     * 获取到所有持久化的消息
     * @return
     * @throws MqttPersistenceException
     */
    public List<BasePacket> getAllNeed2Retry() throws MqttPersistenceException;

    /**
     * 保存消息
     * @param key
     * @param object
     * @throws MqttPersistenceException
     */
    public void save(String key ,Object object) throws MqttPersistenceException;

    /**
     * 移除消息
     * @param string
     * @throws MqttPersistenceException
     */
    public void remove(String string) throws MqttPersistenceException;

}
