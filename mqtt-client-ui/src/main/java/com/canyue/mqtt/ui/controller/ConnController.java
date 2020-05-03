package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.core.client.impl.MqttClient;
import com.canyue.mqtt.core.eventobject.ClientStatusEvent;
import com.canyue.mqtt.core.eventobject.MessageEvent;
import com.canyue.mqtt.core.exception.MqttException;
import com.canyue.mqtt.core.listener.ClientStatusListener;
import com.canyue.mqtt.core.listener.MessageReceivedListener;
import com.canyue.mqtt.core.persistence.impl.FilePersistence;
import com.canyue.mqtt.ui.config.ConnConfig;
import com.canyue.mqtt.ui.data.DataFactory;
import com.canyue.mqtt.ui.data.DataHolder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

/**
 * @author canyue
 */
public class ConnController  {
    @FXML
    private TextField tfSocket;
    @FXML
    private Button btnConnect;
    @FXML
    private Button btnDisconnect;
    @FXML
    private Button btnSettings;

    private static Logger logger = LoggerFactory.getLogger(ConnController.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("E yyyy-MM-dd hh:mm:ss a zzz");
    private ClientController clientController;
    private DataHolder dataHolder;
    private ListView<Message> lvMsg;
    private TabPane tabPane;



    public void connect(ActionEvent actionEvent) {
        logger.debug("正在连接建立");
        initLvMsg();
        try {
            ConnConfig connConfig = dataHolder.getConnConfig();
            tfSocket.setText(
                    connConfig.getHost()+":"+connConfig.getPort());
            MqttClient client = MqttClient.getBuilder()
                    .setHost(connConfig.getHost())
                    .setPort(connConfig.getPort())
                    .setMessageReceivedListener(new MyMessageReceivedListener())
                    .setClientStatusListener(new MyClientStatusListener())
                    .setPersistence(new FilePersistence())
                    .build();
            dataHolder.setMqttClient(client);
            client.start();
            client.connect(connConfig.getUsername(),connConfig.getPassword(),connConfig.getClientId(),null,connConfig.getKeepAlive(),connConfig.isCleanSession());
            logger.debug("连接已建立");
            this.dataHolder.setRunStatus(true);
        } catch (MqttException e) {
            logger.error("连接失败:",e);
        }
    }

    public void disconnect(ActionEvent actionEvent) {
        try {
            logger.debug("正在断开连接");
            dataHolder.getMqttClient().disconnect();
            btnDisconnect.setDisable(true);
            btnConnect.setDisable(false);
            tabPane.setDisable(true);
            btnSettings.setDisable(false);
            lvMsg.getItems().removeAll();
            dataHolder.setRunStatus(false);
            logger.info("连接已断开！");
        } catch (MqttException e) {
            logger.error("断开连接失败：",e);
        }
    }

    public void settings(ActionEvent actionEvent) {
        FXMLLoader configFxmlLoader = new FXMLLoader();
        configFxmlLoader.setLocation(getClass().getClassLoader().getResource("layout/config.fxml"));
        Scene scene;
        try {
            scene= new Scene(configFxmlLoader.load());
            Stage configStage =  new Stage();
            configStage.setScene(scene);
            ConfigController configController = configFxmlLoader.getController();
            configController.setStage(configStage);
            configController.setConnConfig(dataHolder.getConnConfig());
            configController.initData();
            configStage.initModality(Modality.WINDOW_MODAL);
            configStage.initOwner(clientController.getMainStage());
            configStage.setTitle("连接配置");
            configStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("MyController.settings");
    }
    public void initLvMsg(){
        lvMsg.setPlaceholder(new Label("没有数据!"));
        lvMsg.setFixedCellSize(60);
        //自定义listView单元格
        lvMsg.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                ListCell<Message> listCell=new ListCell<Message>(){
                    @Override
                    protected void updateItem(Message item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty==false){
                            HBox hBox=new HBox(10);
                            Label topic = new Label(item.getTopic());
                            Label msgIdLabel =new Label(item.getMsgId()+"");
                            Label qosLabel = new Label(item.getQos()+"");
                            Label payloadLabel = null;
                            try {
                                payloadLabel = new Label(new String(item.getPayload(),"utf8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            //Button bu = new Button(item.getMsgId()+"");
                            hBox.getChildren().addAll(topic,msgIdLabel,qosLabel,payloadLabel);
                            this.setGraphic(hBox);

                        }
                    }
                };
                return listCell;
            }
        });
    }

    public void injectMainController(ClientController clientController) {
        System.out.println("ConnController.injectMainController");
        this.clientController = clientController;
        init();
    }

    private void init() {
        this.lvMsg = this.clientController.getListView();
        this.tabPane = this.clientController.getTabPane();
        dataHolder = DataFactory.dataMap.get(clientController);
        System.out.println(clientController + "====conn");
        System.out.println(dataHolder);
    }

    @FXML
    private void initialize(){
    }

    class MyMessageReceivedListener implements MessageReceivedListener {
        @Override
        public void messageArrived(MessageEvent messageEvent) {
            //在子线程更新UI，不然会报java.lang.IllegalStateException: Not on FX application thread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lvMsg.getItems().add(messageEvent.getMessage());
                }
            });
        }
    }
    class MyClientStatusListener implements ClientStatusListener {
        @Override
        public void connectCompeted(ClientStatusEvent clientStatusEvent) {
            btnConnect.setDisable(true);
            btnDisconnect.setDisable(false);
            tabPane.setDisable(false);
            btnSettings.setDisable(true);
        }

        @Override
        public void shutdown(ClientStatusEvent clientStatusEvent) {
            btnDisconnect.setDisable(true);
            btnConnect.setDisable(false);
            tabPane.setDisable(true);
            btnSettings.setDisable(false);
            lvMsg.getItems().removeAll();
            logger.info("连接已断开！");
        }
    }
}


