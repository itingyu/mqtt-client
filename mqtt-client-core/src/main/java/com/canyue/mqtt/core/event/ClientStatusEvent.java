package com.canyue.mqtt.core.event;

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
    public final static int SUBSCRIBE_COMPUTED = 2;
    public final static int UNSUBSCRIBE_COMPUTED = 3;
    private int statusCode;
    private String[] topicFilters = null;

    public ClientStatusEvent(ClientStatusEventSource source, int statusCode) {
        super(source);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String[] getTopicFilters() {
        return topicFilters;
    }

    public void setTopicFilters(String[] topicFilters) {
        this.topicFilters = topicFilters;
    }
}
