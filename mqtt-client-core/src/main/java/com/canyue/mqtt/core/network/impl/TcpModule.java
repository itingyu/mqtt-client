package com.canyue.mqtt.core.network.impl;

import com.canyue.mqtt.core.network.INetworkModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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
	
	public void start() throws IOException {
		
		SocketAddress address=new InetSocketAddress(host,port);
		this.socket=new Socket();
		socket.connect(address,timeout*1000);
		//设置一次读取的最大时长
		//socket.setSoTimeout(1000);
		System.out.println("本地socket："+ socket.getLocalSocketAddress());
	}
	
	public void stop() throws IOException {
		if(socket!=null)
			socket.close();
	}
	
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}
	
	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}
}
