package com.canyue.mqtt.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MainFrame extends Application {

    public static void main(String[] args) {
       Application.launch(MainFrame.class,args);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("layout/main.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MQTT客户端测试工具1.0");
        primaryStage.getIcons().add(new Image("images/mqtt.png"));
        primaryStage.show();
    }
}
