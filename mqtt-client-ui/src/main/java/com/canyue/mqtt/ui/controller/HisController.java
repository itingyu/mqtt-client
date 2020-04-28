package com.canyue.mqtt.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class HisController{
    @FXML
    private TextArea ta_history;
    private MainController mainController;

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }
    @FXML
    private void initialize(){
    }
}
