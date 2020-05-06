package com.canyue.mqtt.ui.component.listcell;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.ui.controller.TopicFilterCellController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.util.Map;

/**
 * @author: canyue
 * @Date: 2020/5/5 19:22
 */
public class TopicFilterCell extends ListCell<String> {
    private final Node graphic;
    private final TopicFilterCellController topicFilterCellController;
    private final Map<String, ObservableList<Message>> map;

    public TopicFilterCell(Map<String, ObservableList<Message>> map) throws IOException {
        this.map = map;
        FXMLLoader loader = new FXMLLoader();
        graphic = loader.load(getClass().getClassLoader().getResourceAsStream("fxml/topicFilter_cell.fxml"));
        topicFilterCellController = loader.getController();
    }

    @Override
    protected void updateItem(String topicFilter, boolean isEmpty) {
        super.updateItem(topicFilter, isEmpty);
        if (isEmpty || topicFilter == null) {
            this.setGraphic(null);
        } else {
            topicFilterCellController.setTopicFilter(topicFilter);
            topicFilterCellController.setMessageList(map.get(topicFilter));
            topicFilterCellController.init();
            this.setGraphic(graphic);
        }
    }

    public TopicFilterCellController getTopicFilterCellController() {
        return topicFilterCellController;
    }
}
