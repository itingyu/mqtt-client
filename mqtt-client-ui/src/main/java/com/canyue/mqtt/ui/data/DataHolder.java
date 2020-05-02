package com.canyue.mqtt.ui.data;

import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.ui.config.ConnConfig;



/**
 * @author canyue
 */
public class DataHolder {
    private MqttClient mqttClient ;
    private ConnConfig connConfig;
    private boolean runStatus;

    public MqttClient getMqttClient() {
        return mqttClient;
    }

    public void setMqttClient(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public ConnConfig getConnConfig() {
        return connConfig;
    }

    public void setConnConfig(ConnConfig connConfig) {
        this.connConfig = connConfig;
    }

    public boolean isRunStatus() {
        return runStatus;
    }

    public void setRunStatus(boolean runStatus) {
        this.runStatus = runStatus;
    }
}
