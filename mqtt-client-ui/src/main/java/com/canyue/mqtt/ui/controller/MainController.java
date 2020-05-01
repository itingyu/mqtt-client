package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.ui.DataHolder;
import com.canyue.mqtt.ui.config.ConnConfig;
import com.canyue.mqtt.ui.DataFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

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

    private Stage mainStage;

    private DataHolder dataHolder = new DataHolder();
    private boolean runState = false;

    @FXML
    private void initialize(){
        dataHolder.setConnConfig(new ConnConfig());
        dataHolder.setMqttClient(new MqttClient());
        connController.injectMainController(this);
        pubController.injectMainController(this);
        subController.injectMainController(this);
        hisController.injectMainController(this);
        DataFactory.dataMap.put(this,dataHolder);
    }
    public DataHolder getDataHolder(){
        return this.dataHolder;
    }

    public TabPane getTabPane() {
        return tabPane;
    }
    public ListView<Message> getListView(){
        return this.subController.getLv_msg();
    }
    public void close(){
        if(runState){
            this.connController.disconnect(null);
            this.setRunState(false);
        }
    }

    public void setRunState(boolean runState) {
        this.runState = runState;
    }
    public void setMainStage(Stage stage){
        this.mainStage=stage;
    }

    public Stage getMainStage() {
        return mainStage;
    }
}
