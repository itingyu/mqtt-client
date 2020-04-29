package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.EventSource.ClientStatusEventSource;
import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.EventSource.MessageEventSource;
import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.core.event_object.ClientStatusEvent;
import com.canyue.mqtt.core.event_object.MessageEvent;
import com.canyue.mqtt.core.exception.MqttException;
import com.canyue.mqtt.core.listener.ClientStatusListener;
import com.canyue.mqtt.core.listener.MessageReceivedListener;
import com.canyue.mqtt.core.persistence.impl.FilePersistence;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

public class ConnController  {
    @FXML
    private TextField tf_socket;
    @FXML
    private Button btn_connect;
    @FXML
    private Button btn_disconnect;
    @FXML
    private Button btn_settings;

    private static Logger logger = LoggerFactory.getLogger(ConnController.class);
    private MessageEventSource messageEventSource = new MessageEventSource();
    private ClientStatusEventSource clientStatusEventSource = new ClientStatusEventSource();
    private SimpleDateFormat sdf = new SimpleDateFormat ("E yyyy-MM-dd hh:mm:ss a zzz");
    private MainController mainController;
    private MqttClient client;
    private ListView<Message> lv_msg;
    private TabPane tabPane;


    public void connect(ActionEvent actionEvent) {
        logger.debug("正在连接建立");
        initLv_msg();
        messageEventSource.setListener(new MessageReceivedListener() {
            public void MessageArrived(MessageEvent messageEvent) {
                //在子线程更新UI，不然会报java.lang.IllegalStateException: Not on FX application thread
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        lv_msg.getItems().add(messageEvent.getMessage());
                    }
                });
            }
        });
        clientStatusEventSource.setListener(new ClientStatusListener() {
            @Override
            public void connectCompeted(ClientStatusEvent clientStatusEvent) {
                btn_connect.setDisable(true);
                btn_disconnect.setDisable(false);
                tabPane.setDisable(false);
                btn_settings.setDisable(true);
            }

            @Override
            public void shutdown(ClientStatusEvent clientStatusEvent) {
                btn_disconnect.setDisable(true);
                btn_connect.setDisable(false);
                tabPane.setDisable(true);
                btn_settings.setDisable(false);
                lv_msg.getItems().removeAll();
                logger.info("连接已断开！");
            }
        });
        try {
            client.setHost("127.0.0.1")
                    .setPort(1883)
                    .setMessageEventSource(messageEventSource)
                    .setClientStatusEventSource(clientStatusEventSource)
                    .setPersistence(new FilePersistence("C:\\Users\\ASUS\\Desktop\\dataDir"))
                    .start();
            client.connect("canyue","123321","MyMqttClientTestTool",null,20,true);
            logger.debug("连接已建立");
            this.mainController.setRunState(true);
            //ta_history.appendText(sdf.format(new Date())+"INFO:  客户端(id:"+"MyMqttClientTestTool"+")连接到服务器\n");
        } catch (MqttException e) {
            logger.error("连接失败:",e);
        }
    }

    public void disconnect(ActionEvent actionEvent) {
        try {
            logger.debug("正在断开连接");
            client.disconnect();
            //ta_history.appendText(sdf.format(new Date())+"INFO: 断开连接\n");
            btn_disconnect.setDisable(true);
            btn_connect.setDisable(false);
            tabPane.setDisable(true);
            btn_settings.setDisable(false);
            lv_msg.getItems().removeAll();
            logger.info("连接已断开！");
        } catch (MqttException e) {
            logger.error("断开连接失败：",e);
        }
    }

    public void settings(ActionEvent actionEvent) {
        System.out.println("MyController.settings");
    }
    public void initLv_msg(){
        lv_msg.setPlaceholder(new Label("没有数据!"));
        lv_msg.setFixedCellSize(60);
        //自定义listView单元格
        lv_msg.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            public ListCell<Message> call(ListView<Message> param) {
                ListCell<Message> listCell=new ListCell<Message>(){
                    @Override
                    protected void updateItem(Message item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty==false){
                            HBox hBox=new HBox(10);
                            Label topic = new Label(item.getTopic());
                            Label msgid_label=new Label(item.getMsgId()+"");
                            Label qos_label = new Label(item.getQos()+"");
                            Label payload_label = null;
                            try {
                                payload_label = new Label(new String(item.getPayload(),"utf8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            //Button bu = new Button(item.getMsgId()+"");
                            hBox.getChildren().addAll(topic,msgid_label,qos_label,payload_label);
                            this.setGraphic(hBox);

                        }
                    }
                };
                return listCell;
            }
        });
    }

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
        init();
    }

    private void init() {
        this.client=this.mainController.getClient();
        this.lv_msg=this.mainController.getListView();
        this.tabPane=this.mainController.getTabPane();
    }

    @FXML
    private void initialize(){
    }
}
