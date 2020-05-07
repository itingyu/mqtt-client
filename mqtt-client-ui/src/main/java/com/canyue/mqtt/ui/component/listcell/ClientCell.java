package com.canyue.mqtt.ui.component.listcell;

import com.canyue.mqtt.ui.component.listcell.cellcontroller.ClientCellController;
import com.canyue.mqtt.ui.data.DataHolder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * @author: canyue
 * @Date: 2020/5/4 20:36
 * 参考：https://stackoverflow.com/questions/36388527/custom-control-fxml-in-listview-cell-in-javafx-8?r=SearchResults
 * 改进了代码
 */
public class ClientCell extends ListCell<DataHolder> {
    private final Node graphic;
    private final ClientCellController clientCellController;

    public ClientCell() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        graphic = loader.load(getClass().getClassLoader().getResourceAsStream("fxml/client_cell.fxml"));
        clientCellController = loader.getController();
    }

    @Override
    protected void updateItem(DataHolder dataHolder, boolean isEmpty) {
        super.updateItem(dataHolder, isEmpty);
        if (isEmpty || dataHolder == null) {
            //如果没有这行代码，就会出现，添加第二个元素时，列表增加了两个一样的元素，https://www.oschina.net/question/2417669_2273033
            setGraphic(null);
        } else {
            clientCellController.setDataHolder(dataHolder);
            clientCellController.initData();
            setGraphic(graphic);
        }
    }
}
