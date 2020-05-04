package com.canyue.mqtt.core.eventsource;

import com.canyue.mqtt.core.event.ClientStatusEvent;
import com.canyue.mqtt.core.listener.ClientStatusListener;

/**
 * @author canyue
 */
public class ClientStatusEventSource {
    private ClientStatusListener listener ;

    public void setListener(ClientStatusListener listener) {
        this.listener = listener;
    }
    public void  notifyListenerEvent(ClientStatusEvent clientStatusEvent){
        int statusCode = clientStatusEvent.getStatusCode();
        if(statusCode==ClientStatusEvent.SHUTDOWN){
            this.listener.shutdown(clientStatusEvent);
        }else if (statusCode == ClientStatusEvent.RUN) {
            this.listener.connectCompeted(clientStatusEvent);
        } else if (statusCode == ClientStatusEvent.SUBSCRIBE_COMPUTED) {
            this.listener.subscribeCompeted(clientStatusEvent);
        } else if (statusCode == ClientStatusEvent.UNSUBSCRIBE_COMPUTED) {
            this.listener.unsubscribeCompeted(clientStatusEvent);
        }
    }
}
