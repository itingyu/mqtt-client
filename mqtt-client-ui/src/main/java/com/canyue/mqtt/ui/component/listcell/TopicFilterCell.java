package com.canyue.mqtt.ui.component.listcell;

import com.canyue.mqtt.ui.component.listcell.cellcontroller.TopicFilterCellController;
import com.canyue.mqtt.ui.data.DataHolder;
import com.canyue.mqtt.ui.data.TopicFilterData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * @author: canyue
 * @Date: 2020/5/5 19:22
 */
public class TopicFilterCell extends ListCell<TopicFilterData> {
    private final Node graphic;
    private final TopicFilterCellController topicFilterCellController;
    private final DataHolder dataHolder;

    public TopicFilterCell(DataHolder dataHolder) throws IOException {
        this.dataHolder = dataHolder;
        FXMLLoader loader = new FXMLLoader();
        graphic = loader.load(getClass().getClassLoader().getResourceAsStream("fxml/topicFilter_cell.fxml"));
        topicFilterCellController = loader.getController();
    }

    @Override
    protected void updateItem(TopicFilterData topicFilterData, boolean isEmpty) {
        super.updateItem(topicFilterData, isEmpty);
        if (isEmpty || topicFilterData == null) {
            this.setGraphic(null);
        } else {
            topicFilterCellController.setTopicFilterData(topicFilterData);
            topicFilterCellController.setMqttClient(dataHolder.getMqttClient());
            topicFilterCellController.initData();
            this.setGraphic(graphic);
        }
    }
}
