package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.ui.config.ConnConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.UUID;

/**
 * @author canyue
 */
public class ConfigController {
    @FXML
    private TextField tfConfigHost;
    @FXML
    private TextField tfConfigPort;
    @FXML
    private TextField tfClientId;
    @FXML
    private TextField tfConfigKeepAlive;
    @FXML
    private RadioButton rbConfigCleanSession;
    @FXML
    private RadioButton rbConfigReconnect;
    @FXML
    private TextField tfConfigUsername;
    @FXML
    private PasswordField pfConfigPassword;
    @FXML
    private Spinner<Label> spConfigWillMessageQos;
    @FXML
    private RadioButton rbConfigWillMessageIsRetain;
    @FXML
    private TextField tfConfigWillMessageTopic;
    @FXML
    private TextArea taConfigWillMessagePayload;
    @FXML
    private Button btnConfigOk;
    @FXML
    private Button btnConfigCancel;

    public volatile boolean add = false;

    private ConnConfig connConfig;

    private Stage stage;

    public void generateClientId(ActionEvent actionEvent) {
        UUID uuid = UUID.randomUUID();
        String clientId = uuid.toString().replaceAll("-", "");
        this.tfClientId.setText(clientId);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void confirmConfig(ActionEvent actionEvent) {
        connConfig.setHost(tfConfigHost.getText());
        connConfig.setPort(Integer.valueOf(tfConfigPort.getText()));
        connConfig.setClientId(tfClientId.getText());
        connConfig.setKeepAlive(Integer.valueOf(tfConfigKeepAlive.getText()));
        connConfig.setCleanSession(rbConfigCleanSession.isSelected());
        connConfig.setReconnect(rbConfigReconnect.isSelected());
        connConfig.setUsername(tfConfigUsername.getText());
        connConfig.setPassword(pfConfigPassword.getText());
       // connConfig.setWillMessageQos();
        connConfig.setWillMessageIsRetain(rbConfigWillMessageIsRetain.isSelected());
        add = true;
        stage.close();
    }

    public void cancelConfig(ActionEvent actionEvent) {
        stage.close();
    }

    public void setConnConfig(ConnConfig connConfig) {
        this.connConfig = connConfig;
    }

    public ConnConfig getConnConfig() {
        return connConfig;
    }

    public void initData() {
        tfConfigHost.setText(connConfig.getHost());
        tfConfigPort.setText(connConfig.getPort() + "");
        tfClientId.setText(connConfig.getClientId());
        tfConfigKeepAlive.setText(connConfig.getKeepAlive() + "");

        rbConfigCleanSession.setSelected(connConfig.isCleanSession());
        rbConfigReconnect.setSelected(connConfig.isReconnect());

        tfConfigUsername.setText(connConfig.getUsername());
        pfConfigPassword.setText(connConfig.getPassword());

        //sp_config_willMessage_qos.set
        rbConfigWillMessageIsRetain.setSelected(connConfig.isWillMessageIsRetain());
    }
}
