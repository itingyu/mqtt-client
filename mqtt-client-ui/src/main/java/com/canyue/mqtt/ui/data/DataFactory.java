package com.canyue.mqtt.ui.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author canyue
 */
public class DataFactory {

    public static final Map<String, DataHolder> clientMap = new HashMap<>();
    public static final ObservableList<DataHolder> dataHolderList = FXCollections.observableArrayList();

    private static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage) {
        DataFactory.mainStage = mainStage;
    }
}
