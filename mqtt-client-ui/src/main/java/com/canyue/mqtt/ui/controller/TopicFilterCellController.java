package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

/**
 * @author: canyue
 * @Date: 2020/5/5 19:25
 */
public class TopicFilterCellController {

    @FXML
    private Label lbTopicFilter;
    @FXML
    private Label lbMessageCount;
    private String topicFilter;
    private int count = 0;
    private List<Message> messageList;

    public void unsubscribe(ActionEvent actionEvent) {
        System.out.println("取消订阅");
    }

    public void setTopicFilter(String topicFilter) {
        this.topicFilter = topicFilter;
    }

    public void init() {
        this.lbTopicFilter.setText(topicFilter);
        this.lbMessageCount.setText(count + "");
    }
}
