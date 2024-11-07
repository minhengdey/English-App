package com.noface.demo.topic;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;

public class Topic {
    private StringProperty title = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();

    public Topic(String title, String description) {
        this.title.set(title);
        this.description.set(description);
    }


    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }
}
