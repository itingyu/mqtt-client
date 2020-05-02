package com.canyue.mqtt.core.callback;


/**
 * @author canyue
 */
public interface ClientCallback  {
    /**
     * 连接完成后回调
     */
    public void connectCompeted();

    /**
     * 重连时回调
     */
    public void reconnect();

    /**
     * 连接被关闭时回调
     */
    public void shutdown();
}
