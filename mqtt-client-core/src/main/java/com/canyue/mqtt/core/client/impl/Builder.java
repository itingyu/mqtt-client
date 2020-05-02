package com.canyue.mqtt.core.client.impl;

import com.canyue.mqtt.core.listener.ClientStatusListener;
import com.canyue.mqtt.core.listener.MessageReceivedListener;
import com.canyue.mqtt.core.listener.impl.DefaultClientStatusListener;
import com.canyue.mqtt.core.listener.impl.DefaultMessageReceivedShower;
import com.canyue.mqtt.core.persistence.IPersistence;
import com.canyue.mqtt.core.persistence.impl.DefaultPersistence;


/**
 * @author canyue
 */
public class Builder {

    private MqttClient mqttClient;

    String host;
    int port;
    IPersistence persistence;
    MessageReceivedListener messageReceivedListener;
    ClientStatusListener clientStatusListener;


    Builder(){
        this.host = "127.0.0.1";
        this.port = 1883;
        this.persistence = new DefaultPersistence();
        this.messageReceivedListener = new DefaultMessageReceivedShower();
        this.clientStatusListener = new DefaultClientStatusListener();

    }

    public Builder setHost(String host){
        if(host==null||"".equals(host)){
            throw new IllegalArgumentException();
        }
        this.host = host;
        return this;
    }
    public Builder setPort(int port){
        this.port = port;
        return this;
    }
    public Builder setPersistence(IPersistence persistence){
        this.persistence=persistence;
        return this;
    }
    public Builder setMessageReceivedListener(MessageReceivedListener messageReceivedListener){
      this.messageReceivedListener=messageReceivedListener;
        return this;
    }
    public Builder setClientStatusListener(ClientStatusListener clientStatusListener){
       this.clientStatusListener=clientStatusListener;
        return this;
    }


    public MqttClient build(){
        return new MqttClient(this);
    }
}
