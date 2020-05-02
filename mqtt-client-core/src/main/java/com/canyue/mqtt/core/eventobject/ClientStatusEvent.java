package com.canyue.mqtt.core.eventobject;

import com.canyue.mqtt.core.eventsource.ClientStatusEventSource;
import java.util.EventObject;


/**
 * @author canyue
 */
public class ClientStatusEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public final static int SHUTDOWN = 0;
    public final static int RUN = 1;
    private int statusCode ;
    public ClientStatusEvent(ClientStatusEventSource source, int statusCode) {
        super(source);
        this.statusCode=statusCode;
    }
    public int getStatusCode() {
        return statusCode;
    }
}
