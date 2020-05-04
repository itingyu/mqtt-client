package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.ui.component.listcell.ClientCell;
import com.canyue.mqtt.ui.config.ConnConfig;
import com.canyue.mqtt.ui.data.DataFactory;
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

    private ConnConfig connConfig;
    private ClientCell clientCell;

    public void remove(ActionEvent actionEvent) {
        ClientController clientController = clientCell.getItem();
        clientController.close();
        clientCell.getListView().getItems().remove(clientController);
        DataFactory.dataMap.remove(clientController);
        System.out.println("remove");
    }

    @FXML
    private void initialize() {
        System.out.println("ClientCellController.initialize");
    }

    public void init() {
        laClientId.setText("clientId:\t" + connConfig.getClientId());
        laAddress.setText("broker address:\t" + connConfig.getHost() + ":" + connConfig.getPort());
        laUsername.setText("username:\t" + connConfig.getUsername());
    }

    public ConnConfig getConnConfig() {
        return connConfig;
    }

    public void setConnConfig(ConnConfig connConfig) {
        this.connConfig = connConfig;
    }

    public void setListCell(ClientCell clientCell) {
        this.clientCell = clientCell;
    }
}
