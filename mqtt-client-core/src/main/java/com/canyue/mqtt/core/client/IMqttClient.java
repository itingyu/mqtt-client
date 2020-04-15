package com.canyue.mqtt.core.client;

import com.canyue.mqtt.core.Message;

import java.io.IOException;

public interface IMqttClient {
	public void start();
	public void connect(String username,String password,String clientId,Message willMessage,int keepAlive,boolean cleanSession) throws IOException;
	public void disconnect() throws IOException;
	public void subscribe(String[] topics,int[] qosList) throws IOException;
	public void unsubscribe(String[] topics) throws IOException;
	public void ping() throws IOException;
	public void publish(Message msg) throws IOException;
}
