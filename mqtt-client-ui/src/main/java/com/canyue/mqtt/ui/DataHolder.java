package com.canyue.mqtt.ui;

import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.ui.config.ConnConfig;

public class DataHolder {
    private MqttClient mqttClient ;
    private ConnConfig connConfig;

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
}
