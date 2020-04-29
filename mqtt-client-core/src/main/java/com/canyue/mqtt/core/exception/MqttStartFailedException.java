package com.canyue.mqtt.core.exception;

public class MqttStartFailedException extends MqttException {
    public MqttStartFailedException(){
        super();
    }
    public MqttStartFailedException(String s) {
       super(s);
    }
    public MqttStartFailedException(Throwable cause){
        super(cause);
    }
    public MqttStartFailedException(String message, Throwable cause){
        super(message,cause);
    }
}
