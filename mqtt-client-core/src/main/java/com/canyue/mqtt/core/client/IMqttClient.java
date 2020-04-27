package com.canyue.mqtt.core.client;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.MessageShower;
import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.core.exception.MqttStartFailedException;

import java.io.IOException;

public interface IMqttClient {
	public void start() throws  MqttStartFailedException;
	public void connect(String username,String password,String clientId,Message willMessage,int keepAlive,boolean cleanSession) throws Exception;
	public void disconnect() throws Exception;
	public void subscribe(String[] topics,int[] qosList) throws Exception;
	public void unsubscribe(String[] topics) throws Exception;
	public void publish(Message msg) throws Exception;
	public MqttClient setMessageShower(MessageShower messageShower);
}
