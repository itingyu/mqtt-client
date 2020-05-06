package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private ObservableList<Message> messageList;

    public void unsubscribe(ActionEvent actionEvent) {
        System.out.println("取消订阅");
    }

    public void setTopicFilter(String topicFilter) {
        this.topicFilter = topicFilter;
    }

    public void init() {
        this.lbTopicFilter.setText(topicFilter);
        messageList.addListener(new ListChangeListener<Message>() {
            @Override
            public void onChanged(Change<? extends Message> c) {
                lbMessageCount.setText(messageList.size() + "");
            }
        });
    }

    @FXML
    private void initialize() {
    }

    public ObservableList<Message> getMessageList() {
        return this.messageList;
    }

    public void setMessageList(ObservableList<Message> messageList) {
        this.messageList = messageList;
    }

    public String getTopicFilter() {
        return topicFilter;
    }
}
