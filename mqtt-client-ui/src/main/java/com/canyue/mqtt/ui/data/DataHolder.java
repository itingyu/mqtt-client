package com.canyue.mqtt.ui.data;

import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.ui.config.ConnConfig;
import com.canyue.mqtt.ui.controller.ClientController;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;


/**
 * @author canyue
 */
public class DataHolder {
    private MqttClient mqttClient;
    private ConnConfig connConfig;
    private boolean runStatus;
    private VBox clientVBox;
    private ObservableList<TopicFilterData> filterDataList;
    private ClientController clientController;

    public VBox getClientVBox() {
        return clientVBox;
    }

    public void setClientVBox(VBox clientVBox) {
        this.clientVBox = clientVBox;
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

    public ObservableList<TopicFilterData> getFilterDataList() {
        return filterDataList;
    }

    public void setFilterDataList(ObservableList<TopicFilterData> filterDataList) {
        this.filterDataList = filterDataList;
    }

    public ClientController getClientController() {
        return clientController;
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }
}
