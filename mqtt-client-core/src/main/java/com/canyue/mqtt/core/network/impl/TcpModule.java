package com.canyue.mqtt.core.network.impl;

import com.canyue.mqtt.core.network.INetworkModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


/**
 * @author canyue
 */
public class TcpModule implements INetworkModule {
	private Socket socket;
	private String host;
	private int port;
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	private int timeout;
	public TcpModule(String host,int port){
		this.host=host;
		this.port=port;
	}
	private static Logger logger= LoggerFactory.getLogger(TcpModule.class);
	@Override
    public void start() throws IOException {
		
		SocketAddress address=new InetSocketAddress(host,port);
		this.socket=new Socket();
		socket.connect(address,timeout*1000);
		logger.info("本地socket：{}",socket.getLocalSocketAddress());
	}
	
	@Override
	public void stop() throws IOException {
		if(socket!=null) {
			socket.close();
		}
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}
}
