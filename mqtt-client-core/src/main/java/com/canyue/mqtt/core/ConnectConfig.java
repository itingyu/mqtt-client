package com.canyue.mqtt.core;




/**
 * @author canyue
 */
public class ConnectConfig {
    private String clientId;
    //遗嘱消息
    private Message willMessage;
    private String userName;
    private String password;
    private boolean cleanSession;
    private int keepAlive = 0;

    public ConnectConfig(String clientId, Message willMessage, String userName, String password, boolean cleanSession, int keepAlive) {
        this.clientId = clientId;
        this.willMessage = willMessage;
        this.userName = userName;
        this.password = password;
        this.cleanSession = cleanSession;
        this.keepAlive = keepAlive;
    }

    public Message getWillMessage() {
        return willMessage;
    }

    public void setWillMessage(Message willMessage) {
        this.willMessage = willMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
