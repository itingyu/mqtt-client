package com.canyue.mqtt.core.EventSource;

import com.canyue.mqtt.core.event_object.ClientStatusEvent;
import com.canyue.mqtt.core.listener.ClientStatusListener;


public class ClientStatusEventSource {
    private ClientStatusListener listener ;

    public void setListener(ClientStatusListener listener) {
        this.listener = listener;
    }
    public void  notifyListenerEvent(ClientStatusEvent clientStatusEvent){
        int statusCode = clientStatusEvent.getStatusCode();
        System.out.println("------------------statusCode-------------"+statusCode);
        if(statusCode==ClientStatusEvent.SHUTDOWN){
            this.listener.shutdown(clientStatusEvent);
        }else if(statusCode==ClientStatusEvent.RUN){
            this.listener.connectCompeted(clientStatusEvent);
        }
    }
}