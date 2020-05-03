package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.ui.data.DataFactory;
import com.canyue.mqtt.ui.data.DataHolder;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

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

    private DataHolder dataHolder = new DataHolder();
    private Stage mainStage;

    @FXML
    private void initialize() {
        System.out.println("============");

        DataFactory.dataMap.put(this, dataHolder);
        System.out.println(dataHolder);
        System.out.println(this + "===");
        connController.injectMainController(this);
        pubController.injectMainController(this);
        subController.injectMainController(this);
        hisController.injectMainController(this);

    }

    public DataHolder getDataHolder() {
        return this.dataHolder;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public ListView<Message> getListView() {
        return this.subController.getLvMsg();
    }

    public void close() {
        if (dataHolder.isRunStatus()) {
            this.connController.disconnect(null);
            this.dataHolder.setRunStatus(false);
        }
        dataHolder.getConnConfig().saveConfig();
    }
}
