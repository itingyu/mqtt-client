package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.ui.config.ConnConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.UUID;

public class ConfigController {
    @FXML
    private TextField tf_config_host;
    @FXML
    private TextField tf_config_port;
    @FXML
    private TextField tf_clientID;
    @FXML
    private TextField tf_config_keepAlive;
    @FXML
    private RadioButton rb_config_cleanSession;
    @FXML
    private RadioButton rb_config_reconnect;
    @FXML
    private TextField tf_config_username;
    @FXML
    private PasswordField pf_config_password;
    @FXML
    private Spinner<Label> sp_config_willMessage_qos;
    @FXML
    private RadioButton rb_config_willMessage_isRetain;
    @FXML
    private TextField tf_config_willMessage_topic;
    @FXML
    private TextArea ta_config_willMessage_payload;
    @FXML
    private Button btn_config_ok;
    @FXML
    private Button btn_config_cancel;

    private ConnConfig connConfig;
    
    private Stage stage;
    public void generateClientID(ActionEvent actionEvent) {
        UUID uuid = UUID.randomUUID();
        String clientID = uuid.toString().replaceAll("-","");
        this.tf_clientID.setText(clientID);
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }


    public void confirmConfig(ActionEvent actionEvent) {
        connConfig.setHost(tf_config_host.getText());
        connConfig.setPort(Integer.valueOf(tf_config_port.getText()));
        connConfig.setClientID(tf_clientID.getText());
        connConfig.setKeepAlive(Integer.valueOf(tf_config_keepAlive.getText()));
        connConfig.setCleanSession(rb_config_cleanSession.isSelected());
        connConfig.setReconnect(rb_config_reconnect.isSelected());
        connConfig.setUsername(tf_config_username.getText());
        connConfig.setPassword(pf_config_password.getText());
       // connConfig.setWillMessageQos();
        connConfig.setWillMessageIsRetain(rb_config_willMessage_isRetain.isSelected());
        stage.close();
    }

    public void cancelConfig(ActionEvent actionEvent) {
        stage.close();
    }

    public void setConnConfig(ConnConfig connConfig) {
        this.connConfig = connConfig;
    }
    public void initData(){
        tf_config_host.setText(connConfig.getHost());
        tf_config_port.setText(connConfig.getPort()+"");
        tf_clientID.setText(connConfig.getClientID());
        tf_config_keepAlive.setText(connConfig.getKeepAlive()+"");

        rb_config_cleanSession.setSelected(connConfig.isCleanSession());
        rb_config_reconnect.setSelected(connConfig.isReconnect());

        tf_config_username.setText(connConfig.getUsername());
        pf_config_password.setText(connConfig.getPassword());

        //sp_config_willMessage_qos.set
        rb_config_willMessage_isRetain.setSelected(connConfig.isWillMessageIsRetain());
    }
}
