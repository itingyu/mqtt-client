package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.core.exception.MqttException;
import com.canyue.mqtt.ui.DataHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SubController  {
    @FXML
    private TextField tf_topicsFilter_sub;
    @FXML
    private Button btn_subscribe;
    @FXML
    private ToggleGroup tg_qos_sub;
    @FXML
    private ListView<Message> lv_msg;
    @FXML
    private TextArea ta_msg_recv;
    private MainController mainController;
    private static Logger logger = LoggerFactory.getLogger(SubController.class);
    private DataHolder dataHolder;

    public void subscribe(ActionEvent actionEvent) {
        logger.debug("subscribe clicked!");
        try {
            int qos =getQosFromTg(tg_qos_sub);
            dataHolder.getMqttClient().subscribe(new String[]{tf_topicsFilter_sub.getText()},new int[]{qos});
            //ta_history.appendText(sdf.format(new Date())+"INFO: 订阅(Topic:"+tf_topicsFilter_sub.getText()+",Qos:"+qos+")\n");
            logger.info("topicsFilters:{},Qos:{}\t消息订阅成功！",tf_topicsFilter_sub.getText(),qos);
        } catch (MqttException e) {
            logger.error("订阅失败:",e);
        }
    }


    private int getQosFromTg(ToggleGroup tg){
        return Integer.parseInt(((RadioButton) tg.getSelectedToggle()).getText().replace("Qos",""));
    }
    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
        init();
    }
    private void init(){
        this.dataHolder=mainController.getDataHolder();

    }
    @FXML
    private void initialize(){
    }

    public ListView<Message> getLv_msg() {
        return lv_msg;
    }
}
