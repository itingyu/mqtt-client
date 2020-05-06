package com.canyue.mqtt.ui.eventhandler;

import com.canyue.mqtt.ui.controller.ClientController;
import com.canyue.mqtt.ui.controller.ConfigController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author: canyue
 * @Date: 2020/5/4 21:30
 */
public class ConfigStageHiddenHandler implements EventHandler<WindowEvent> {
    private ConfigController configController;
    private ListView<ClientController> lvClient;
    private Stage mainStage;

    public ConfigStageHiddenHandler(ConfigController configController, Stage mainStage, ListView<ClientController> lvClient) {
        this.configController = configController;
        this.lvClient = lvClient;
        this.mainStage = mainStage;
    }

    @Override
    public void handle(WindowEvent event) {
        if (configController.add) {
            FXMLLoader clientLoader = new FXMLLoader();
            clientLoader.setLocation(getClass().getClassLoader().getResource("fxml/client.fxml"));
            VBox clientVBox = null;
            try {
                clientVBox = clientLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ClientController clientController = clientLoader.getController();
            clientController.setMainStage(mainStage);
            clientController.getDataHolder().setConnConfig(configController.getConnConfig());
            clientController.getDataHolder().setClientLoader(clientLoader);
            clientController.getDataHolder().setClientVBox(clientVBox);
            clientController.getDataHolder().setMap(new HashMap<>());
            lvClient.getItems().add(clientController);
        }
        configController.add = false;
    }
}
