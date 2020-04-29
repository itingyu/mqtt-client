package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.core.exception.MqttException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        logger.debug("publish clicked!");
        RadioButton rb = (RadioButton) tg_qos_pub.getSelectedToggle();
        int qos= getQosFromTg(tg_qos_pub);
            //ta_history.appendText(sdf.format(new Date())+"INFO: 发布信息("+msg+")\n");
            try {
                //client.publish(msg);
                client.publish(tf_topic_pub.getText(),ta_msg_pub.getText().getBytes(),qos,rb_retained.isSelected());
                logger.info("message:{},Qos:{}\t消息发布成功！",qos);
            } catch (MqttException e) {
                logger.error("发布失败:",e);
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
