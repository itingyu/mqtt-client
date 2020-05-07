package com.canyue.mqtt.ui.data;

import com.canyue.mqtt.core.Message;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * @author: canyue
 * @Date: 2020/5/7 12:18
 */
public class TopicFilterData {
    private String filter;
    private ObservableList<Message> messageObservableList;
    private SimpleIntegerProperty messageCount = new SimpleIntegerProperty();

    public TopicFilterData(String filter, ObservableList<Message> messageObservableList) {
        this.filter = filter;
        this.messageObservableList = messageObservableList;
        messageObservableList.addListener(new ListChangeListener<Message>() {
            @Override
            public void onChanged(Change<? extends Message> c) {
                System.out.println(filter + "消息数目发生变化");
                messageCount.set(messageObservableList.size());
            }
        });
        //size.set(messageObservableList.size());
    }

    public ObservableList<Message> getMessageObservableList() {
        return messageObservableList;
    }

    public void setMessageObservableList(ObservableList<Message> messageObservableList) {
        this.messageObservableList = messageObservableList;
    }

    public int getMessageCount() {
        return messageCount.get();
    }

    public void setMessageCount(int messageCount) {
        this.messageCount.set(messageCount);
    }

    public SimpleIntegerProperty messageCountProperty() {
        return messageCount;
    }

    public String getFilter() {
        return this.filter;
    }
}
