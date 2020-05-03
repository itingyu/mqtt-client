package com.canyue.mqtt.ui.data;

import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.ui.config.ConnConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;


/**
 * @author canyue
 */
public class DataHolder {
    private MqttClient mqttClient;
    private ConnConfig connConfig;
    private boolean runStatus;
    private VBox clientVBox;
    private FXMLLoader clientLoader;

    public VBox getClientVBox() {
        return clientVBox;
    }

    public void setClientVBox(VBox clientVBox) {
        this.clientVBox = clientVBox;
    }

    public FXMLLoader getClientLoader() {
        return clientLoader;
    }

    public void setClientLoader(FXMLLoader clientLoader) {
        this.clientLoader = clientLoader;
    }

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
