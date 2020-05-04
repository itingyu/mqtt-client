package com.canyue.mqtt.ui.component.listcell;

import com.canyue.mqtt.ui.controller.TopicFilterCellController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * @author: canyue
 * @Date: 2020/5/5 19:22
 */
public class TopicFilterCell extends ListCell<String> {
    private final Node graphic;
    private final TopicFilterCellController topicFilterCellController;

    public TopicFilterCell() throws IOException {
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
            topicFilterCellController.init();
            this.setGraphic(graphic);
        }
    }
}
