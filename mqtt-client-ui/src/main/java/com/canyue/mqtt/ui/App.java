package com.canyue.mqtt.ui;

import com.canyue.mqtt.ui.controller.MainController;
import com.canyue.mqtt.ui.data.DataFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

/**
 * @author canyue
 */
public class App extends Application {

    public static void main(String[] args) {
       Application.launch(App.class,args);
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        URL url = App.class.getClassLoader().getResource("fxml/main.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(url);
        Scene scene = new Scene(fxmlLoader.load());
        mainStage.setScene(scene);
        mainStage.setTitle("MQTT客户端测试工具1.0");
        mainStage.getIcons().add(new Image("img/mqtt_icon.png"));
        mainStage.centerOnScreen();
        mainStage.setResizable(false);
        mainStage.show();
        MainController mainController = fxmlLoader.getController();
        //存储主舞台
        DataFactory.setMainStage(mainStage);
        mainStage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                mainController.close();
                Platform.exit();
            }
        });
    }
}
