package com.canyue.mqtt.ui.eventhandler;

import com.canyue.mqtt.ui.config.ConnConfig;
import com.canyue.mqtt.ui.controller.ClientController;
import com.canyue.mqtt.ui.controller.ConfigController;
import com.canyue.mqtt.ui.data.DataFactory;
import com.canyue.mqtt.ui.data.DataHolder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

import java.io.IOException;


/**
 * @author: canyue
 * @Date: 2020/5/4 21:30
 */
public class ConfigStageHiddenHandler implements EventHandler<WindowEvent> {
    private ConfigController configController;
    private StackPane stackPane;

    public ConfigStageHiddenHandler(ConfigController configController, StackPane stackPane) {
        this.stackPane = stackPane;
        this.configController = configController;
    }

    @Override
    public void handle(WindowEvent event) {
        if (configController.add) {
            ConnConfig connConfig = configController.getConnConfig();
            //如果存在该标识符的客户端，则不再创建
            if (DataFactory.clientMap.containsKey(connConfig.getClientId())) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("提示");
                        alert.setHeaderText("");
                        alert.setContentText("客户端标识符为:" + connConfig.getClientId() + "的MQTT客户端已存在，禁止重复创建");
                        alert.show();
                    }
                });
                return;
            }
            try {
                //创建一个MQTT客户端
                VBox clientVBox = null;
                FXMLLoader clientLoader = new FXMLLoader();
                clientLoader.setLocation(getClass().getClassLoader().getResource("fxml/client.fxml"));
                clientVBox = clientLoader.load();
                ClientController clientController = clientLoader.getController();
                //创建数据载体
                DataHolder dataHolder = new DataHolder();
                dataHolder.setClientController(clientController);
                dataHolder.setConnConfig(configController.getConnConfig());
                dataHolder.setClientVBox(clientVBox);
                stackPane.getChildren().add(clientVBox);
                dataHolder.setFilterDataList(FXCollections.observableArrayList());

                clientController.setDataHolder(dataHolder);
                //存储数据
                DataFactory.clientMap.put(connConfig.getClientId(), dataHolder);
                DataFactory.dataHolderList.add(dataHolder);
                clientController.init();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        configController.add = false;
    }
}
