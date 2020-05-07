package com.canyue.mqtt.ui.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private boolean willMessageIsRetain;
    private static final Logger logger = LoggerFactory.getLogger(ConnConfig.class);
    private Properties properties = new Properties();
    private File dir;
     public ConnConfig(){

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

    public static ConnConfig getDefaultConnConfig() {
        ConnConfig connConfig = new ConnConfig();
        try {
            connConfig.properties.load(ConnConfig.class.getClassLoader().getResourceAsStream("default_config.properties"));
            logger.info("加载默认配置文件:");
            Properties properties = connConfig.properties;
            Set<String> strings = properties.stringPropertyNames();
            for (String s : strings) {
                logger.debug("{}={}", s, properties.getProperty(s));
            }
            connConfig.host = properties.getProperty("host", "127.0.0.1");
            connConfig.port = Integer.valueOf(properties.getProperty("port", "1883"));
            connConfig.clientId = properties.getProperty("clientID");
            connConfig.keepAlive = Integer.valueOf(properties.getProperty("keepAlive", "20"));
            connConfig.cleanSession = Boolean.valueOf(properties.getProperty("cleanSession", "true"));
            connConfig.reconnect = Boolean.valueOf(properties.getProperty("reconnect", "true"));
            connConfig.username = properties.getProperty("username", "");
            connConfig.password = properties.getProperty("password", "");
            connConfig.willMessageQos = Integer.valueOf(properties.getProperty("willMessage.qos", "0"));
            connConfig.willMessageIsRetain = Boolean.valueOf(properties.getProperty("willMessage.isRetain", "true"));
        } catch (IOException e) {
            logger.error("配置文件加载失败！", e);
        }
        return connConfig;
    }

    public void saveConfig() {
        properties.setProperty("host", host);
        properties.setProperty("port", String.valueOf(port));
        properties.setProperty("clientID", clientId);
        properties.setProperty("keepAlive", String.valueOf(keepAlive));
        properties.setProperty("cleanSession", String.valueOf(cleanSession));
        properties.setProperty("reconnect", String.valueOf(reconnect));
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        properties.setProperty("willMessage.qos", String.valueOf(willMessageQos));
        properties.setProperty("willMessage.isRetain", String.valueOf(willMessageIsRetain));
        try {
            dir = new File(System.getProperty("user.dir"), ".mqtt/" + clientId);
            if (!dir.exists() || !dir.isFile()) {
                dir.mkdirs();
            }
            properties.store(new FileOutputStream(new File(dir, "/config.properties")), clientId + " connect config");
        } catch (IOException e) {
            logger.info("配置保存失败",e);
        }
    }
}
