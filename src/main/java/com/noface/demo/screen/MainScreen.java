package com.noface.demo.screen;

import com.noface.demo.Controller.MainController;
import com.noface.demo.card.Card;
import com.noface.demo.resource.ResourceLoader;
import javafx.beans.property.ListProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class MainScreen {
    private final FXMLLoader loader;
    private MainController mainController;



    public MainScreen(MainController mainController, Pane listTopicPane, Pane cardTopicPane, Pane cardLearningPane) throws IOException {
        this.mainController = mainController;
        loader = new FXMLLoader(this.getClass().getResource("MainScreen.fxml"));
        loader.setController(this);
        this.listTopicPane = listTopicPane;
        this.cardTopicPane = cardTopicPane;
        this.cardLearningPane = cardLearningPane;
        loader.load();
    }

    private Pane listTopicPane, cardLearningPane, cardTopicPane;
    public <T> T getRoot(){
        return loader.getRoot();
    }

    @FXML
    private Button dictionaryButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button topicButton;
    @FXML
    private AnchorPane rightPane;

    @FXML
    private Button translateButton;
    @FXML
    public void initialize() throws IOException {
        initSubScreen();
        configureScreenComponentEvent();
    }
    public void configureScreenComponentEvent(){
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        translateButton.setOnAction(translateButtonClickedEventHandler());
        topicButton.setOnAction(topicButtonClickedEventHanlder());
        profileButton.setOnAction(profileButtonClickedEventHandler());
    }

    private EventHandler<ActionEvent> profileButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        };
    }


    private EventHandler<ActionEvent> topicButtonClickedEventHanlder() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeToListTopicPane();
            }
        };
    }

    private TranslateScreen translateScreen;
    private ListTopicScreen listTopicScreen;
    private ProfileScreen profileScreen;
    private CardTopicScreen cardTopicScreen;
    private CardLearningScreen cardLearningScreen;
    public void initSubScreen() throws IOException {
    }


    private EventHandler<ActionEvent> translateButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeToTranslatePane();
            }
        };
    }
    public void changeToListTopicPane(){
        rightPane.getChildren().clear();
        rightPane.getChildren().add(listTopicPane);
    }
    public void changeToCardTopicPane(){
        rightPane.getChildren().clear();
        rightPane.getChildren().add(cardTopicPane);
    }

    private void changeToTranslatePane() {

    }


}
