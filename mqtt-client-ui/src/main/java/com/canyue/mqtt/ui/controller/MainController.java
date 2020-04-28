package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.client.impl.MqttClient;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;

public class MainController {
    @FXML
    private TabPane tabPane;
    @FXML
    private ConnController connController;
    @FXML
    private PubController pubController;
    @FXML
    private SubController subController;
    @FXML
    private HisController hisController;

    private MqttClient client = new MqttClient();

    @FXML
    private void initialize(){
        connController.injectMainController(this);
        pubController.injectMainController(this);
        subController.injectMainController(this);
        hisController.injectMainController(this);
    }
    public MqttClient getClient(){
        return this.client;
    }

    public TabPane getTabPane() {
        return tabPane;
    }
    public ListView<Message> getListView(){
        return this.subController.getLv_msg();
    }
}
