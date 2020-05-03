package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.exception.MqttException;
import com.canyue.mqtt.ui.data.DataHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
    private ClientController clientController;

    private static Logger logger = LoggerFactory.getLogger(PubController.class);
    private DataHolder dataHolder;

    public void publish(ActionEvent actionEvent) {
        logger.debug("publish clicked!");
        RadioButton rb = (RadioButton) tgQosPub.getSelectedToggle();
        int qos= getQosFromTg(tgQosPub);
        try {
            dataHolder.getMqttClient().publish(tfTopicPub.getText(), taMsgPub.getText().getBytes(), qos, rbRetain.isSelected());
            logger.info("message:{},Qos:{}\t消息发布成功！", qos);
        } catch (MqttException e) {
            logger.error("发布失败:", e);
        }
    }

    private int getQosFromTg(ToggleGroup tg) {
        return Integer.parseInt(((RadioButton) tg.getSelectedToggle()).getText().replace("Qos", ""));
    }

    public void injectMainController(ClientController clientController) {
        System.out.println("PubController.injectMainController");
        this.clientController = clientController;
        init();
    }

    private void init() {
        this.dataHolder = this.clientController.getDataHolder();
    }

    @FXML
    private void initialize() {
    }
}
