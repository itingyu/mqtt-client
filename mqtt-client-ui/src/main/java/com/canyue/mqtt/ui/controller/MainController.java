package com.canyue.mqtt.ui.controller;

import com.canyue.mqtt.ui.config.ConnConfig;
import com.canyue.mqtt.ui.data.DataFactory;
import com.canyue.mqtt.ui.data.DataHolder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @author: canyue
 * @Date: 2020/5/3 18:21
 */
public class MainController {

    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    @FXML
    private ListView<ClientController> lvClient;

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
        configFxmlLoader.setLocation(getClass().getClassLoader().getResource("layout/config.fxml"));
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
            configStage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    if (configController.add) {
                        FXMLLoader clientLoader = new FXMLLoader();
                        clientLoader.setLocation(getClass().getClassLoader().getResource("layout/client.fxml"));
                        VBox clientVBox = null;
                        try {
                            System.out.println(clientLoader.getController() + "====");
                            clientVBox = clientLoader.load();
                            System.out.println("===========================");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ClientController clientController = clientLoader.getController();
                        clientController.setMainStage(mainStage);
                        clientController.getDataHolder().setConnConfig(configController.getConnConfig());
                        clientController.getDataHolder().setClientLoader(clientLoader);
                        clientController.getDataHolder().setClientVBox(clientVBox);
                        DataFactory.dataMap.put(clientController, clientController.getDataHolder());
                        System.out.println(clientLoader.getController() + "===new ");
                        lvClient.getItems().add(clientController);
                    } else {
                        configController.add = false;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {

    }

    private void showConfig() {

    }


    @FXML
    private void initialize() {
        initLvMsg();
    }

    public void initLvMsg() {
        lvClient.setPlaceholder(new Label("没有数据!"));
        lvClient.setFixedCellSize(60);
        //自定义listView单元格
        lvClient.setCellFactory(new Callback<ListView<ClientController>, ListCell<ClientController>>() {
            @Override
            public ListCell<ClientController> call(ListView<ClientController> param) {
                ListCell<ClientController> listCell = new ListCell<ClientController>() {
                    @Override
                    protected void updateItem(ClientController item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty == false) {
                            Label label = new Label("client");
                            //Label label = new Label(item.getDataHolder().getConnConfig().getClientId());
                            this.setGraphic(label);
                        }
                    }
                };
                listCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        DataHolder dataHolder = DataFactory.dataMap.get(listCell.getItem());
                        System.out.println(listCell.getItem() + "==listCell  before");
                        FXMLLoader clientLoader = dataHolder.getClientLoader();
                        if (clientLoader == null) {
                            System.out.println(dataHolder.getConnConfig() + " handle");
                            clientLoader = new FXMLLoader();
                            clientLoader.setLocation(getClass().getClassLoader().getResource("layout/client.fxml"));
                            VBox clientVBox = null;
                            clientLoader.setController(listCell.getItem());
                            try {
                                System.out.println(clientLoader.getController() + "==before");
                                clientVBox = clientLoader.load();
                                System.out.println(clientLoader.getController() + "==after");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            dataHolder.setClientLoader(clientLoader);
                            dataHolder.setClientVBox(clientVBox);
                        }
                        System.out.println(listCell.getItem() + "==listCell after");
                        System.out.println(dataHolder);
                        borderPane.setRight(dataHolder.getClientVBox());
                    }
                });
                return listCell;
            }
        });

    }
}

