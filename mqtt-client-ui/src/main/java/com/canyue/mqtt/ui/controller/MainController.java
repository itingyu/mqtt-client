package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.ui.component.listcell.ClientCell;
import com.canyue.mqtt.ui.config.ConnConfig;
import com.canyue.mqtt.ui.data.DataFactory;
import com.canyue.mqtt.ui.data.DataHolder;
import com.canyue.mqtt.ui.eventhandler.ConfigStageHiddenHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;


/**
 * @author: canyue
 * @Date: 2020/5/3 18:21
 */
public class MainController {

    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    @FXML
    private ListView<ClientController> lvClient;
    @FXML
    private ImageView iv_bg;

    private Stage mainStage;
    @FXML
    private BorderPane borderPane;

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void create(ActionEvent actionEvent) {
        FXMLLoader configFxmlLoader = new FXMLLoader();
        configFxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/config.fxml"));
        Scene scene;
        try {
            scene = new Scene(configFxmlLoader.load());
            Stage configStage = new Stage();
            configStage.setScene(scene);
            ConfigController configController = configFxmlLoader.getController();
            configController.setStage(configStage);
            configController.setConnConfig(ConnConfig.getDefaultConnConfig());
            configController.initData();
            configStage.initModality(Modality.WINDOW_MODAL);
            configStage.initOwner(mainStage);
            configStage.setTitle("连接配置");
            configStage.show();
            configStage.setOnHidden(new ConfigStageHiddenHandler(configController, mainStage, lvClient));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        Set<ClientController> clientControllerSet = DataFactory.dataMap.keySet();
        for (ClientController clientController : clientControllerSet) {
            clientController.close();
            clientController = null;
        }
    }
    @FXML
    private void initialize() {
        initLvMsg();
    }

    public void initLvMsg() {
        lvClient.setPlaceholder(new Label("没有MQTT客户端!"));
        lvClient.setFixedCellSize(100);
        //自定义listView单元格
        lvClient.setCellFactory(new Callback<ListView<ClientController>, ListCell<ClientController>>() {
            @Override
            public ListCell<ClientController> call(ListView<ClientController> param) {
                try {
                    ListCell<ClientController> listCell = new ClientCell();
                    listCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (!listCell.isEmpty()) {
                                String name = event.getButton().name();
                                if (MouseButton.PRIMARY.name().equals(name)) {
                                    DataHolder dataHolder = DataFactory.dataMap.get(listCell.getItem());
                                    borderPane.setCenter(dataHolder.getClientVBox());
                                }
                                System.out.println(event.getButton().name() + " click");
                            } else {
                                System.out.println("blank click!");
                            }
                        }
                    });
                    return listCell;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        lvClient.setEditable(false);
        lvClient.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        lvClient.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ClientController>() {
            @Override
            public void changed(ObservableValue<? extends ClientController> observable, ClientController oldValue, ClientController newValue) {
                System.out.println("???????");
            }
        });
        lvClient.getItems().addListener(new ListChangeListener<ClientController>() {
            @Override
            public void onChanged(Change<? extends ClientController> c) {
                if (lvClient.getItems().isEmpty()) {
                    borderPane.setCenter(iv_bg);
                    return;
                } else {
                    c.next();
                    if (c.wasRemoved()) {
                        ClientController clientController = lvClient.getSelectionModel().getSelectedItem();
                        if (clientController != null) {
                            borderPane.setCenter(DataFactory.dataMap.get(clientController).getClientVBox());
                        }
                    }
                }
            }
        });

    }
}

