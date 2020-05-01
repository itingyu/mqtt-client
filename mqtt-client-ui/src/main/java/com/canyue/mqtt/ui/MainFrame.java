package com.canyue.mqtt.ui;

import com.canyue.mqtt.ui.controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;


public class MainFrame extends Application {

    public static void main(String[] args) {
       Application.launch(MainFrame.class,args);
    }

    public void start(Stage primaryStage) throws Exception {

        URL url = MainFrame.class.getClassLoader().getResource("layout/main.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(url);
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("MQTT客户端测试工具1.0");
        primaryStage.getIcons().add(new Image("img/mqtt.png"));
        primaryStage.show();
        MainController mainController = fxmlLoader.getController();
        mainController.setMainStage(primaryStage);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                mainController.close();
                Platform.exit();
            }
        });
    }
}
