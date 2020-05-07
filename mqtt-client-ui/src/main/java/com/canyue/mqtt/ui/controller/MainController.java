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
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;


/**
 * @author: canyue
 * @Date: 2020/5/3 18:21
 */
public class MainController {

    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    @FXML
    private ListView<DataHolder> lvClient;
    @FXML
    private ImageView iv_bg;
    @FXML
    private StackPane stackPane;

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
            configStage.initOwner(DataFactory.getMainStage());
            configStage.setTitle("连接配置");
            configStage.show();
            configStage.setOnHidden(new ConfigStageHiddenHandler(configController, stackPane));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        Set<String> clientIdSet = DataFactory.clientMap.keySet();
        for (String clientId : clientIdSet) {
            DataFactory.clientMap.get(clientId).getClientController().close();
        }
    }
    @FXML
    private void initialize() {
        stackPane.getChildren().add(iv_bg);
        initLvMsg();
        lvClient.setItems(DataFactory.dataHolderList);
    }

    public void initLvMsg() {
        lvClient.setPlaceholder(new Label("没有MQTT客户端!"));
        lvClient.setFixedCellSize(100);
        //自定义listView单元格
        lvClient.setCellFactory(new Callback<ListView<DataHolder>, ListCell<DataHolder>>() {
            @Override
            public ListCell<DataHolder> call(ListView<DataHolder> param) {
                try {
                    ClientCell clientCell = new ClientCell();
                    clientCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (!clientCell.isEmpty()) {
                                String name = event.getButton().name();
                                if (MouseButton.PRIMARY.name().equals(name)) {
                                    clientCell.updateSelected(true);
                                }
                            }
                        }
                    });
                    clientCell.selectedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if (newValue == true) {
                                clientCell.getItem().getClientVBox().toFront();
                            }
                        }
                    });
                    return clientCell;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        lvClient.setEditable(false);
        lvClient.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lvClient.getItems().addListener(new ListChangeListener<DataHolder>() {
            @Override
            public void onChanged(Change<? extends DataHolder> c) {
                c.next();
                if (c.wasRemoved()) {
                    Iterator iterator = c.getRemoved().iterator();
                    System.out.println("移除了" + c.getRemovedSize() + "个客户端");
                    while (iterator.hasNext()) {
                        DataHolder dh = (DataHolder) iterator.next();
                        stackPane.getChildren().remove(dh.getClientVBox());
                    }
                }
            }
        });
    }
}

