package com.canyue.mqtt.core.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface INetworkModule {
	public void start() throws IOException;
	public void stop() throws IOException;
	public InputStream getInputStream() throws IOException;
	public OutputStream getOutputStream() throws IOException;
}