package com.canyue.mqtt.core.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * @author canyue
 */
public interface INetworkModule {
	/**
	 * 网络模块启动
	 * @throws IOException
	 */
	public void start() throws IOException;

	/**
	 * 网络模块关掉
	 * @throws IOException
	 */
	public void stop() throws IOException;

	/**
	 * 网络输入流
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException;

	/**
	 * 网络输出流
	 * @return
	 * @throws IOException
	 */
	public OutputStream getOutputStream() throws IOException;
}