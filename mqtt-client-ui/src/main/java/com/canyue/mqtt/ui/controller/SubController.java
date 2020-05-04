package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.exception.MqttException;
import com.canyue.mqtt.ui.component.listcell.MessageCell;
import com.canyue.mqtt.ui.component.listcell.TopicFilterCell;
import com.canyue.mqtt.ui.data.DataHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @author canyue
 */
public class SubController {
    @FXML
    private TextField tfTopicsFilterSub;
    @FXML
    private ToggleGroup tgQosSub;
    @FXML
    private ListView<Message> lvMessage;
    @FXML
    private ListView<String> lvTopicFilter;
    @FXML
    private TextArea taMsgRecv;
    private ClientController clientController;
    private static Logger logger = LoggerFactory.getLogger(SubController.class);
    private DataHolder dataHolder;

    public void subscribe(ActionEvent actionEvent) {
        logger.debug("subscribe clicked!");
        try {
            int qos = getQosFromTg(tgQosSub);
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
        initLvTopicFilters();
        initLvMessage();
    }

    public ListView<Message> getLvMessage() {
        return lvMessage;
    }

    public ListView<String> getLvTopicFilter() {
        return lvTopicFilter;
    }

    private void initLvMessage() {
        lvMessage.setPlaceholder(new Label("没有数据!"));
        lvMessage.setFixedCellSize(60);
        //自定义listView单元格
        lvMessage.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                try {
                    ListCell<Message> listCell = new MessageCell();
                    return listCell;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private void initLvTopicFilters() {
        lvTopicFilter.setPlaceholder(new Label("没有数据!"));
        lvTopicFilter.setFixedCellSize(60);
        //自定义listView单元格
        lvTopicFilter.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                try {
                    ListCell<String> listCell = new TopicFilterCell();
                    return listCell;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}
