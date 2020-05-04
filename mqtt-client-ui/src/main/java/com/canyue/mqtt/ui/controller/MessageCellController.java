package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


/**
 * @author: canyue
 * @Date: 2020/5/5 16:15
 */
public class MessageCellController {
    @FXML
    private Label lbTopic;
    @FXML
    private Label lbQos;
    private Message message;

    public void setMessage(Message message) {
        this.message = message;
    }

    public void init() {
        this.lbTopic.setText("topic:\t" + message.getTopic());
        this.lbQos.setText("Qos:\t" + message.getQos());
    }
}
