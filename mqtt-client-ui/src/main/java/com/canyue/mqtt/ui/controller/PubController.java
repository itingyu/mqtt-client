package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.client.impl.MqttClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PubController {
    @FXML
    private TextField tf_topic_pub;
    @FXML
    private Button btn_publish;
    @FXML
    private ToggleGroup tg_qos_pub;
    @FXML
    private RadioButton rb_retained;
    @FXML
    private TextArea ta_msg_pub;
    private MainController mainController;

    private static Logger logger = LoggerFactory.getLogger(PubController.class);
    private MqttClient client;

    public void publish(ActionEvent actionEvent) {
        logger.info("publish clicked!");
        RadioButton rb = (RadioButton) tg_qos_pub.getSelectedToggle();

        Message msg = new Message(tf_topic_pub.getText());
        msg.setPayload(ta_msg_pub.getText().getBytes());
        msg.setRetain(rb_retained.isSelected());
        int qos=getQosFromTg(tg_qos_pub);
        msg.setQos(qos);
        //ta_history.appendText(sdf.format(new Date())+"INFO: 发布信息("+msg+")\n");
        try {
            client.publish(msg);
            logger.info("message:{},Qos:{}",msg,qos);
        } catch (IOException e) {
            logger.error("发布失败:",e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int getQosFromTg(ToggleGroup tg){
        return Integer.parseInt(((RadioButton) tg.getSelectedToggle()).getText().replace("Qos",""));
    }
    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
        init();
    }

    private void init() {
        this.client=this.mainController.getClient();
    }

    @FXML
    private void initialize(){
    }
}
