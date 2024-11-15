package com.noface.demo.screen;

import com.noface.demo.controller.MainController;
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

public class MainScreen {
    private final FXMLLoader loader;
    private MainController mainController;
    private Stage stage;

    private Pane listTopicPane, cardLearningPane, cardTopicPane, translatePane, profilePane;

    public MainScreen(MainController mainController, Pane listTopicPane,
                      Pane cardTopicPane, Pane cardLearningPane, Pane translatePane,
                      Pane profilePane) throws IOException {
        this.mainController = mainController;

        this.listTopicPane = listTopicPane;
        this.cardTopicPane = cardTopicPane;
        this.cardLearningPane = cardLearningPane;
        this.translatePane = translatePane;
        this.profilePane = profilePane;

        loader = new FXMLLoader(this.getClass().getResource("MainScreen.fxml"));
        loader.setController(this);
        loader.load();
    }

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
                changeToProfilePane();
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
    public void changeToCardTopicPane(String topic){
        mainController.getTopicScreenController().loadCardByTopic(topic);
        rightPane.getChildren().clear();
        rightPane.getChildren().add(cardTopicPane);
    }

    public void changeToTranslatePane() {
        rightPane.getChildren().clear();
        rightPane.getChildren().add(translatePane);
    }
    public void changeToProfilePane(){
        rightPane.getChildren().clear();
        rightPane.getChildren().add(profilePane);
    }
    public void changeToMainScreen(){

    }

}
