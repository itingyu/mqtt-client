package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.ui.data.DataHolder;
import com.canyue.mqtt.ui.data.TopicFilterData;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;


/**
 * @author canyue
 */
public class ClientController {

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
    @FXML
    private DataHolder dataHolder;

    @FXML
    private void initialize() {
        System.out.println("ClientController.initialize");
    }

    public void init() {
        connController.injectMainController(this);
        pubController.injectMainController(this);
        subController.injectMainController(this);
        hisController.injectMainController(this);
    }

    public DataHolder getDataHolder() {
        return this.dataHolder;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public ListView<Message> getLvMessage() {
        return this.subController.getLvMessage();
    }

    public ListView<TopicFilterData> getLvTopicFilter() {
        return this.subController.getLvTopicFilter();
    }

    public void close() {
        if (dataHolder.isRunStatus()) {
            this.connController.disconnect(null);
            this.dataHolder.setRunStatus(false);
        }
        dataHolder.getConnConfig().saveConfig();
    }

    public void setDataHolder(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }
}
