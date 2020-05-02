package com.canyue.mqtt.ui.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.Set;

/**
 * @author canyue
 */
public class ConnConfig {
    private  String host;
    private  int port;
    private  String clientId;
    private  int keepAlive;
    private  boolean cleanSession;
    private  boolean reconnect;
    private  String username;
    private  String password;
    private  int willMessageQos;
    private  boolean willMessageIsRetain;
    private  final Logger logger= LoggerFactory.getLogger(ConnConfig.class);
    private Properties properties= new Properties();
    private File file;
     public ConnConfig(){
             String path = ConnConfig.class.getClassLoader().getResource("config/ConnConfig.properties").getPath();
                InputStream inputStream = null;
             System.out.println(path);
             try {
                 file = new File(path);
                 inputStream = new FileInputStream(file);
                 properties.load(inputStream);
                 Set<String>  strings=properties.stringPropertyNames();
                 for (String s : strings) {
                     System.out.println(s+"======="+properties.getProperty(s));
                 }
                 host=properties.getProperty("host","127.0.0.1");
                 port=Integer.valueOf(properties.getProperty("port","1883"));
                 clientId=properties.getProperty("clientID");
                 keepAlive=Integer.valueOf(properties.getProperty("keepAlive","20"));
                 cleanSession=Boolean.valueOf(properties.getProperty("cleanSession","true"));
                 reconnect=Boolean.valueOf(properties.getProperty("reconnect","true"));
                 username=properties.getProperty("username","");
                 password=properties.getProperty("password","");
                 willMessageQos=Integer.valueOf(properties.getProperty("willMessage.qos","0"));
                 willMessageIsRetain=Boolean.valueOf(properties.getProperty("willMessage.isRetain","true"));
             } catch (IOException e) {
                 logger.error("配置文件加载失败！",e);
             }finally {
                 try {
                     inputStream.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
     }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public boolean isReconnect() {
        return reconnect;
    }

    public void setReconnect(boolean reconnect) {
        this.reconnect = reconnect;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWillMessageQos() {
        return willMessageQos;
    }

    public void setWillMessageQos(int willMessageQos) {
        this.willMessageQos = willMessageQos;
    }

    public boolean isWillMessageIsRetain() {
        return willMessageIsRetain;
    }

    public void setWillMessageIsRetain(boolean willMessageIsRetain) {
        this.willMessageIsRetain = willMessageIsRetain;
    }
    public void saveConfig(){
        properties.setProperty("host",host);
        properties.setProperty("port", String.valueOf(port));
        properties.setProperty("clientID",clientId);
        properties.setProperty("keepAlive", String.valueOf(keepAlive));
        properties.setProperty("cleanSession", String.valueOf(cleanSession));
        properties.setProperty("reconnect", String.valueOf(reconnect));
        properties.setProperty("username",username);
        properties.setProperty("password",password);
        properties.setProperty("willMessage.qos", String.valueOf(willMessageQos));
        properties.setProperty("willMessage.isRetain", String.valueOf(willMessageIsRetain));

        try {
            properties.store(new FileOutputStream(file),"保存");
        } catch (IOException e) {
            logger.info("配置保存失败",e);
        }
    }
}
