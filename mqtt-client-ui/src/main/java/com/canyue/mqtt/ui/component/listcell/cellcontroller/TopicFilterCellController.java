package com.canyue.mqtt.ui.component.listcell.cellcontroller;

import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.core.exception.MqttException;
import com.canyue.mqtt.ui.data.TopicFilterData;
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

    private TopicFilterData topicFilterData;
    private MqttClient mqttClient;

    public void unsubscribe(ActionEvent actionEvent) {
        System.out.println("取消订阅");
        try {
            mqttClient.unsubscribe(new String[]{topicFilterData.getFilter()});
            topicFilterData.getMessageObservableList().clear();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void initData() {
        this.lbTopicFilter.setText(topicFilterData.getFilter());
        lbMessageCount.textProperty().bind(topicFilterData.messageCountProperty().asString());
    }

    @FXML
    private void initialize() {
    }

    public void setMqttClient(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void setTopicFilterData(TopicFilterData topicFilterData) {
        this.topicFilterData = topicFilterData;
    }
}
