package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.exception.MqttException;
import com.canyue.mqtt.ui.component.listcell.MessageCell;
import com.canyue.mqtt.ui.component.listcell.TopicFilterCell;
import com.canyue.mqtt.ui.data.DataHolder;
import com.canyue.mqtt.ui.data.TopicFilterData;
import com.canyue.mqtt.ui.utils.Encoder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


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
    private ListView<TopicFilterData> lvTopicFilter;
    @FXML
    private TextArea taMsgRecv;
    @FXML
    private ComboBox<String> cbEncode;

    private byte[] payloadTemp = null;

    private ClientController clientController;
    private static Logger logger = LoggerFactory.getLogger(SubController.class);
    private DataHolder dataHolder;

    public void subscribe(ActionEvent actionEvent) {
        logger.debug("subscribe clicked!");
        try {
            int qos = getQosFromTg(tgQosSub);
            dataHolder.getMqttClient().subscribe(new String[]{tfTopicsFilterSub.getText()}, new int[]{qos});
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
        lvTopicFilter.setItems(dataHolder.getFilterDataList());
        dataHolder.getFilterDataList().addListener(new ListChangeListener<TopicFilterData>() {
            @Override
            public void onChanged(Change<? extends TopicFilterData> c) {
                if (lvTopicFilter.getItems().isEmpty()) {
                    System.out.println("清空================");
                    lvMessage.getItems().clear();
                }
            }
        });


    }

    @FXML
    private void initialize() {
        initLvTopicFilters();
        initLvMessage();
        cbEncode.getItems().addAll("utf-8", "hex", "bin", "gbk");
        cbEncode.getSelectionModel().selectFirst();
        cbEncode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (payloadTemp != null) {
                    try {
                        taMsgRecv.setText(Encoder.encode(payloadTemp, newValue));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public ListView<Message> getLvMessage() {
        return lvMessage;
    }

    public ListView<TopicFilterData> getLvTopicFilter() {
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
                    MessageCell messageCell = new MessageCell();
                    messageCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (!messageCell.isEmpty()) {
                                String name = event.getButton().name();
                                if (MouseButton.PRIMARY.name().equals(name)) {
                                    messageCell.updateSelected(true);
                                }
                                logger.debug(event.getButton().name() + " click");
                            } else {
                                logger.debug("blank click!");
                            }
                        }
                    });
                    return messageCell;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        lvMessage.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observable, Message oldValue, Message newValue) {
                if (newValue != null) {
                    try {
                        payloadTemp = newValue.getPayload();
                        taMsgRecv.setText(Encoder.encode(payloadTemp, cbEncode.getValue()));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initLvTopicFilters() {
        lvTopicFilter.setPlaceholder(new Label("没有数据!"));
        lvTopicFilter.setFixedCellSize(60);
        //自定义listView单元格
        lvTopicFilter.setCellFactory(new Callback<ListView<TopicFilterData>, ListCell<TopicFilterData>>() {
            @Override
            public ListCell<TopicFilterData> call(ListView<TopicFilterData> param) {
                try {
                    TopicFilterCell topicFilterCell = new TopicFilterCell(dataHolder);
                    topicFilterCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (!topicFilterCell.isEmpty()) {
                                String name = event.getButton().name();
                                if (MouseButton.PRIMARY.name().equals(name)) {
                                    if (lvMessage.getItems().size() <= 0) {
                                        taMsgRecv.setText("");
                                        payloadTemp = null;
                                    } else {
                                        lvMessage.getSelectionModel().select(0);
                                    }
                                }
                                logger.debug(event.getButton().name() + " click");
                            } else {
                                logger.debug("blank click!");
                            }
                        }
                    });
                    return topicFilterCell;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        lvTopicFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TopicFilterData>() {
            @Override
            public void changed(ObservableValue<? extends TopicFilterData> observable, TopicFilterData oldValue, TopicFilterData newValue) {
                if (newValue != null) {
                    lvMessage.setItems(newValue.getMessageObservableList());
                    lvMessage.getSelectionModel().selectFirst();
                    lvMessage.getItems().addListener(new ListChangeListener<Message>() {
                        @Override
                        public void onChanged(Change<? extends Message> c) {
                            if (lvMessage.getItems().isEmpty()) {
                                payloadTemp = null;
                                taMsgRecv.setText("");
                            }
                        }
                    });
                }
            }
        });
        lvTopicFilter.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
}
