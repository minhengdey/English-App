package com.noface.demo.screen.component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.geometry.Pos;

public class TopicBar extends HBox {
    private Button editButton;
    private Button learnButton;
    private Button removeButton;
    private String topicName;

    public TopicBar(String topicName) {
        this.getStyleClass().add("list-title-hbox");
        this.topicName = topicName;
        Label label = new Label(topicName);

        editButton = new Button("Edit");

        learnButton = new Button("Learn");

        removeButton = new Button("Remove");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        this.getChildren().addAll(label, spacer, editButton, learnButton, removeButton);
    }


    public void setOnLearnButtonClicked(EventHandler<ActionEvent> evt){
        learnButton.setOnAction(evt);

    }
    public void setOnEditButtonClicked(EventHandler evt){
        editButton.setOnAction(evt);
    }
    public void setOnRemoveButtonClicked(EventHandler evt){
        removeButton.setOnAction(evt);
    }
    public String getTopicName() {
        return topicName;
    }
}
