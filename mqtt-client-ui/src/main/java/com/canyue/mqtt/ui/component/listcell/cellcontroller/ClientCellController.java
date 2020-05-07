package com.canyue.mqtt.ui.component.listcell.cellcontroller;

import com.canyue.mqtt.ui.config.ConnConfig;
import com.canyue.mqtt.ui.controller.ClientController;
import com.canyue.mqtt.ui.data.DataFactory;
import com.canyue.mqtt.ui.data.DataHolder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * @author: canyue
 * @Date: 2020/5/4 19:02
 */
public class ClientCellController {
    @FXML
    private Label laClientId;
    @FXML
    private Label laAddress;
    @FXML
    private Label laUsername;
    private DataHolder dataHolder;
    private ConnConfig connConfig;

    public void remove(ActionEvent actionEvent) {
        ClientController clientController = dataHolder.getClientController();
        clientController.close();
        DataFactory.clientMap.remove(connConfig.getClientId());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                DataFactory.dataHolderList.remove(dataHolder);
            }
        });

    }

    @FXML
    private void initialize() {
    }

    public void initData() {
        connConfig = dataHolder.getConnConfig();
        laClientId.setText("clientId:\t" + connConfig.getClientId());
        laAddress.setText("broker address:\t" + connConfig.getHost() + ":" + connConfig.getPort());
        laUsername.setText("username:\t" + connConfig.getUsername());
    }

    public void setDataHolder(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }
}
