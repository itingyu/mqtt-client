package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.ui.data.DataHolder;
import com.canyue.mqtt.ui.config.ConnConfig;
import com.canyue.mqtt.ui.data.DataFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import java.util.Objects;

/**
 * @author canyue
 */
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
        return this.subController.getLvMsg();
    }
    public void close(){
        if(dataHolder.isRunStatus()){
            this.connController.disconnect(null);
            this.dataHolder.setRunStatus(false);
        }
        dataHolder.getConnConfig().saveConfig();
    }

    public void setMainStage(Stage stage){
        this.mainStage=stage;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MainController that = (MainController) o;
        return Objects.equals(tabPane, that.tabPane) &&
                Objects.equals(connController, that.connController) &&
                Objects.equals(pubController, that.pubController) &&
                Objects.equals(subController, that.subController) &&
                Objects.equals(hisController, that.hisController) &&
                Objects.equals(mainStage, that.mainStage) &&
                Objects.equals(dataHolder, that.dataHolder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tabPane, connController, pubController, subController, hisController, mainStage, dataHolder);
    }
}
