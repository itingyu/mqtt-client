package com.canyue.mqtt.core.callback;

public interface ClientCallback  {
    public void connectCompeted();
    public void reconnect();
    public void shutdown();
}
