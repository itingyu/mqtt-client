package com.canyue.mqtt.core.client;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.exception.MqttException;
import com.sun.istack.internal.NotNull;


/**
 * @author canyue
 */
public interface IMqttClient {
	/**
	 * 客户端启动方法，socket连接
	 * @throws MqttException
	 */
	public void start() throws MqttException;

	/**
	 * 客户端连接方法
	 * @param username 用户名
	 * @param password 密码
	 * @param clientId 客户端id
	 * @param willMessage 遗嘱消息
	 * @param keepAlive  保持连接
	 * @param cleanSession 清理会话
	 * @throws MqttException
	 */
	public void connect(String username, String password, @NotNull String clientId, Message willMessage, int keepAlive, boolean cleanSession) throws MqttException;

	/**
	 * 断开客户端与broker的连接
	 * @throws MqttException
	 */
	public void disconnect() throws MqttException;

	/**
	 * 消息订阅
	 * @param topicsFilters
	 * @param qosList
	 * @throws MqttException
	 */
	public void subscribe(String[] topicsFilters,int[] qosList) throws MqttException;

	/**
	 * 取消订阅
	 * @param topicsFilters
	 * @throws MqttException
	 */
	public void unsubscribe(String[] topicsFilters) throws MqttException;

	/**
	 * 发布消息
	 * @param topicName
	 * @param payload
	 * @param qos
	 * @param isRetain
	 * @throws MqttException
	 */
	public void publish(String topicName, byte[] payload, int qos, boolean isRetain) throws MqttException;
}
