package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.exception.MqttException;
import com.canyue.mqtt.ui.data.DataHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author canyue
 */
public class PubController {
    @FXML
    private TextField tfTopicPub;
    @FXML
    private ToggleGroup tgQosPub;
    @FXML
    private RadioButton rbRetain;
    @FXML
    private TextArea taMsgPub;
    private MainController mainController;

    private static Logger logger = LoggerFactory.getLogger(PubController.class);
    private DataHolder dataHolder;

    public void publish(ActionEvent actionEvent) {
        logger.debug("publish clicked!");
        RadioButton rb = (RadioButton) tgQosPub.getSelectedToggle();
        int qos= getQosFromTg(tgQosPub);
            try {
                dataHolder.getMqttClient().publish(tfTopicPub.getText(),taMsgPub.getText().getBytes(),qos,rbRetain.isSelected());
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
        this.dataHolder=this.mainController.getDataHolder();
    }

    @FXML
    private void initialize(){
    }
}
