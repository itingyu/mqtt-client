package com.canyue.mqtt.core.exception;


/**
 * @author canyue
 */
public class MqttIllegalArgumentException extends MqttException {
    public MqttIllegalArgumentException(){
        super();
    }
    public MqttIllegalArgumentException(String s){
        super(s);
    }
    public MqttIllegalArgumentException(Throwable cause){
        super(cause);
    }
    public MqttIllegalArgumentException(String message, Throwable cause){
        super(message,cause);
    }
}
