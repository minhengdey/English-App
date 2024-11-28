package com.noface.englishapp.view.component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class TopicBar extends HBox {
    private Button editButton;
    private Button learnButton;
    private Button removeButton;
    private Button renameButton;
    private String topicName;
    private Label label;

    public TopicBar(String topicName) {
        this.getStyleClass().add("list-title-hbox");
        this.topicName = topicName;
        label = new Label(topicName);

        editButton = new Button("Edit");

        learnButton = new Button("Learn");

        removeButton = new Button("Remove");

        renameButton = new Button("Rename");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        this.getChildren().addAll(label, spacer, renameButton, editButton, learnButton, removeButton);
    }
    public void updateTopicTitle(String title){
        topicName = title;
        label.setText(title);
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
    public void setOnRenameButtonClicked(EventHandler evt){
        renameButton.setOnAction(evt);
    }
    public String getTopicName() {
        return topicName;
    }
}
