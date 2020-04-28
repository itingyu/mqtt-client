package com.canyue.mqtt.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class MainFrame extends Application {

    public static void main(String[] args) {
       Application.launch(MainFrame.class,args);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(MainFrame.class.getClassLoader().getResource("layout/main.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MQTT客户端测试工具1.0");
        primaryStage.getIcons().add(new Image("img/mqtt.png"));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
            }
        });
    }
}
