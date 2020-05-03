package com.canyue.mqtt.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


/**
 * @author canyue
 */
public class HisController {
    @FXML
    private TextArea taHistory;
    private ClientController clientController;

    public void injectMainController(ClientController clientController) {
        System.out.println("HisController.injectMainController");
        this.clientController = clientController;
    }

    @FXML
    private void initialize() {
    }
}
