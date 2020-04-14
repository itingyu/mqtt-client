package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.client.impl.MqttClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MyController {
    @FXML
    Button btn_connect;
    @FXML
    Button btn_disconnect;
    @FXML
    TabPane tabPane;
    @FXML
    Button btn_settings;
    @FXML
    ToggleGroup tg_qos_pub;
    @FXML
    ToggleGroup tg_qos_sub;
    @FXML
    RadioButton rb_retained;
    @FXML
    TextField tf_topic_pub;
    @FXML
    TextArea ta_msg_pub;
    @FXML
    TextArea ta_history;
    @FXML
    TextField tf_topic_sub;
    @FXML
    TextArea ta_msg_recv;
    @FXML
    ListView<Message> lv_msgs;

    MqttClient client = new MqttClient();
    SimpleDateFormat sdf = new SimpleDateFormat ("E yyyy-MM-dd hh:mm:ss a zzz");

    public void connect(ActionEvent actionEvent) {
        System.out.println("MyController.connect");
        btn_connect.setDisable(true);
        btn_disconnect.setDisable(false);
        tabPane.setDisable(false);
        btn_settings.setDisable(true);
        try {
            client.start();
            client.connect("canyue","123321","MyMqttClientTestTool",null,60,true);
            ta_history.appendText(sdf.format(new Date())+"INFO:  客户端(id:"+"MyMqttClientTestTool"+")连接到服务器\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disconnect(ActionEvent actionEvent){
        System.out.println("MyController.disconnect");
        btn_disconnect.setDisable(true);
        btn_connect.setDisable(false);
        tabPane.setDisable(true);
        btn_settings.setDisable(false);
        try {
            client.disconnect();
            ta_history.appendText(sdf.format(new Date())+"INFO: 断开连接\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void settings(ActionEvent actionEvent) {
        System.out.println("MyController.settings");
    }

    public void publish(ActionEvent actionEvent) {
        System.out.println("MyController.publish");
        RadioButton rb = (RadioButton) tg_qos_pub.getSelectedToggle();
        System.out.println("Qos:"+rb.getText());
        System.out.println("Retained:"+rb_retained.isSelected());
        Message msg = new Message(tf_topic_pub.getText());
        msg.setPayload(ta_msg_pub.getText().getBytes());
        msg.setRetain(rb_retained.isSelected());
        msg.setQos(getQosFromTg(tg_qos_pub));
        ta_history.appendText(sdf.format(new Date())+"INFO: 发布信息("+msg+")\n");
        try {
            client.publish(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(ActionEvent actionEvent) {
        System.out.println("MyController.subscribe");
        try {
            client.subscribe(new String[]{tf_topic_sub.getText()},new int[]{getQosFromTg(tg_qos_sub)});
            ta_history.appendText(sdf.format(new Date())+"INFO: 订阅(Topic:"+tf_topic_sub.getText()+",Qos:"+getQosFromTg(tg_qos_sub)+")\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int getQosFromTg(ToggleGroup tg){
        return Integer.parseInt(((RadioButton) tg.getSelectedToggle()).getText().replace("Qos",""));
    }
}
