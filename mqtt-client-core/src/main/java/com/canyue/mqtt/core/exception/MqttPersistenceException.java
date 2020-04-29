package com.canyue.mqtt.core.exception;

import java.io.IOException;

public class MqttPersistenceException extends MqttException {
    public MqttPersistenceException(){
        super();
    }
    public MqttPersistenceException(String s) {
        super(s);
    }
    public MqttPersistenceException(Throwable cause){
        super(cause);
    }
    public MqttPersistenceException(String message, Throwable cause){
        super(message,cause);
    }
}
