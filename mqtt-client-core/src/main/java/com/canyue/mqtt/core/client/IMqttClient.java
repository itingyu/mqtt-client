package com.canyue.mqtt.core.client;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.EventSource.MessageEventSource;
import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.core.exception.MqttException;


public interface IMqttClient {
	public void start() throws MqttException;
	public void connect(String username,String password,String clientId,Message willMessage,int keepAlive,boolean cleanSession) throws MqttException;
	public void disconnect() throws MqttException;
	public void subscribe(String[] topicsFilters,int[] qosList) throws MqttException;
	public void unsubscribe(String[] topicsFilters) throws MqttException;
	public void publish(String topicName, byte[] payload, int qos, boolean isRetain) throws MqttException;
	public MqttClient setMessageEventSource(MessageEventSource messageEventSource);
}
