package com.canyue.mqtt.ui.component.listcell;

import com.canyue.mqtt.core.Message;
import com.canyue.mqtt.ui.controller.MessageCellController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * @author: canyue
 * @Date: 2020/5/5 16:13
 */
public class MessageCell extends ListCell<Message> {
    private final Node graphic;
    private final MessageCellController messageCellController;

    public MessageCell() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        graphic = loader.load(getClass().getClassLoader().getResourceAsStream("fxml/message_cell.fxml"));
        messageCellController = loader.getController();
    }

    @Override
    protected void updateItem(Message message, boolean isEmpty) {
        super.updateItem(message, isEmpty);
        if (isEmpty || message == null) {
            this.setGraphic(null);
        } else {
            messageCellController.setMessage(message);
            messageCellController.init();
            this.setGraphic(graphic);
        }
    }
}
