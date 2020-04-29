package com.canyue.mqtt.core.exception;

import java.io.IOException;

public class MqttException extends Exception {
    public MqttException(){
        super();
    }
    public MqttException(String s){
        super(s);
    }
    public MqttException(Throwable cause){
        super(cause);
    }
    public MqttException(String message, Throwable cause){
        super(message,cause);
    }
}
