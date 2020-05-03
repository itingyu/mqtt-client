package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
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
public class SubController  {
    @FXML
    private TextField tfTopicsFilterSub;
    @FXML
    private ToggleGroup tgQosSub;
    @FXML
    private ListView<Message> lvMsg;
    @FXML
    private TextArea taMsgRecv;
    private ClientController clientController;
    private static Logger logger = LoggerFactory.getLogger(SubController.class);
    private DataHolder dataHolder;

    public void subscribe(ActionEvent actionEvent) {
        logger.debug("subscribe clicked!");
        try {
            int qos =getQosFromTg(tgQosSub);
            dataHolder.getMqttClient().subscribe(new String[]{tfTopicsFilterSub.getText()},new int[]{qos});
            logger.info("topicsFilters:{},Qos:{}\t消息订阅成功！", tfTopicsFilterSub.getText(), qos);
        } catch (MqttException e) {
            logger.error("订阅失败:", e);
        }
    }


    private int getQosFromTg(ToggleGroup tg) {
        return Integer.parseInt(((RadioButton) tg.getSelectedToggle()).getText().replace("Qos", ""));
    }

    public void injectMainController(ClientController clientController) {
        System.out.println("SubController.injectMainController");
        this.clientController = clientController;
        init();
    }

    private void init() {
        this.dataHolder = clientController.getDataHolder();

    }

    @FXML
    private void initialize() {
    }

    public ListView<Message> getLvMsg() {
        return lvMsg;
    }
}
