package com.noface.demo.screen.component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;

public class TopicBar extends HBox {
    private Button editButton;
    private Button learnButton;
    public TopicBar(String topicName) {
        VBox.setMargin(this, new Insets(20, 20, 20, 20));
        Label label = new Label(topicName);
        label.setFont(new Font("Arial", 18)); // Đặt kích thước font cho tiêu đề

        HBox.setHgrow(label, Priority.ALWAYS);

        editButton = new Button("Edit");
        editButton.setPrefSize(100, 30); // Đặt kích thước cho nút sửa

        learnButton = new Button("Learn");
        learnButton.setPrefSize(100, 30);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(label, spacer, editButton, learnButton);

        this.setSpacing(10);
        this.setAlignment(Pos.CENTER_LEFT);

        this.setPrefSize(400, 50);
    }

    public void setOnLearnButtonClicked(EventHandler<ActionEvent> evt){
        learnButton.setOnAction(evt);

    }
    public void setOnEditButtonClicked(EventHandler evt){
        editButton.setOnAction(evt);
    }


}
