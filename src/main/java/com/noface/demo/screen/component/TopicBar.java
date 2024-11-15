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

public class TopicBar extends StackPane {
    private Button editButton;
    private Button learnButton;
    private HBox aboveLayer;
    private String topicName;

    public TopicBar(String topicName) {
        final int arcValue = 25;
        this.topicName = topicName;
        final String fillColor = "#FFE6A5";
        VBox.setMargin(this, new Insets(5, 5, 5, 5));
        Label label = new Label(topicName);
        label.setFont(new Font("Arial", 18)); // Đặt kích thước font cho tiêu đề

        HBox.setHgrow(label, Priority.ALWAYS);

        editButton = new Button("Edit");
        editButton.setPrefSize(100, 30); // Đặt kích thước cho nút sửa

        learnButton = new Button("Learn");
        learnButton.setPrefSize(100, 30);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        aboveLayer = new HBox();
        aboveLayer.getChildren().addAll(label, spacer, editButton, learnButton);
        aboveLayer.setPadding(new Insets(30, 30, 30, 30));
        aboveLayer.setSpacing(10);
        aboveLayer.setAlignment(Pos.CENTER_LEFT);

        aboveLayer.setPrefSize(400, 50);

        Rectangle back = new Rectangle();
        back.heightProperty().bind(aboveLayer.heightProperty());
        back.widthProperty().bind(aboveLayer.widthProperty());
        back.setFill(Color.valueOf(fillColor));
        back.setArcHeight(arcValue);
        back.setArcWidth(arcValue);

        this.getChildren().addAll(back, aboveLayer);
    }


    public void setOnLearnButtonClicked(EventHandler<ActionEvent> evt){
        learnButton.setOnAction(evt);

    }
    public void setOnEditButtonClicked(EventHandler evt){
        editButton.setOnAction(evt);
    }

    public String getTopicName() {
        return topicName;
    }
}
