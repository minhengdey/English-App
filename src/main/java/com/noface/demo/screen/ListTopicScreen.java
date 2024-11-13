package com.noface.demo.screen;

import com.noface.demo.Controller.MainController;
import com.noface.demo.card.Card;
import com.noface.demo.Controller.CardLearningController;
import com.noface.demo.screen.component.TopicBar;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ListTopicScreen {
    private  FXMLLoader loader;
    private MainScreen mainScreen;
    private MainController mainController;
    private ListProperty<String> topics =  new SimpleListProperty<>();

    public ListTopicScreen(MainScreen mainScreen, MainController mainController) throws IOException {
        this.mainController = mainController;
        this.mainScreen = mainScreen;
        loader = new FXMLLoader(this.getClass().getResource("ListTopicScreen.fxml"));
        loader.setController(this);
        loader.load();
    }

    public <T> T getRoot() {
        return loader.getRoot();
    }

    @FXML
    private VBox topicBarPane;

    public void connect(ListProperty<String> topics){
        this.topics.bindBidirectional(topics);
        addTopicBarToScreen();
        this.topics.addListener((observable, oldValue, newValue) -> {
            for(String s : newValue){
                TopicBar topicBar = new TopicBar(s);
                topicBarPane.getChildren().add(topicBarPane);
                topicBar.setOnEditButtonClicked(editButtonClickedEventHandler());
                topicBar.setOnLearnButtonClicked(learnButtonClickedEventHandler());
            }
        });
    }
    public void addTopicBarToScreen(){
        topicBarPane.getChildren().clear();
        for(String title : topics.get()){
            TopicBar topicBar = new TopicBar(title);
            topicBarPane.getChildren().add(topicBar);
            topicBar.setOnEditButtonClicked(editButtonClickedEventHandler());
            topicBar.setOnLearnButtonClicked(learnButtonClickedEventHandler());
        }
    }
    private EventHandler<ActionEvent> learnButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        };
    }

    private EventHandler<ActionEvent> editButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainScreen.changeToEditCardTopicScreen();
            }
        };
    }
}
