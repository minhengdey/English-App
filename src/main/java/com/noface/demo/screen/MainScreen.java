package com.noface.demo.screen;

import com.noface.demo.Controller.MainController;
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

import java.io.IOException;

public class MainScreen {
    private final FXMLLoader loader;
    private MainController mainController;



    public MainScreen(MainController mainController) throws IOException {
        this.mainController = mainController;
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
        initSubScreen();
        connect();
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
                rightPane.getChildren().clear();
                rightPane.getChildren().add(profileScreen.getRoot());
            }
        };
    }


    private EventHandler<ActionEvent> topicButtonClickedEventHanlder() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rightPane.getChildren().clear();
                rightPane.getChildren().add(listTopicScreen.getRoot());
            }
        };
    }

    private TranslateScreen translateScreen;
    private ListTopicScreen listTopicScreen;
    private ProfileScreen profileScreen;
    private CardTopicScreen cardTopicScreen;
    private CardLearningScreen cardLearningScreen;
    public void initSubScreen() throws IOException {
        translateScreen = (new TranslateScreen());
        listTopicScreen = (new ListTopicScreen(this, mainController));
        profileScreen = (new ProfileScreen());
        cardTopicScreen = (new CardTopicScreen(this));
        cardLearningScreen = new CardLearningScreen();
    }
    public void connect(){
        listTopicScreen.connect(mainController.topicsProperty());
    }

    private EventHandler<ActionEvent> translateButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeToTranslatePane();
            }
        };
    }

    private void changeToTranslatePane() {
        rightPane.getChildren().clear();
        rightPane.getChildren().add(translateScreen.getRoot());
    }

    public void setFitPane(Pane pane){
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
    }

    public void changeToEditCardTopicScreen(){
        rightPane.getChildren().clear();
        rightPane.getChildren().add(cardTopicScreen.getRoot());
        cardTopicScreen.connect(ResourceLoader.getInstance().getCardsSampleData());
    }

    public void changeToListTopicScreen() {
        rightPane.getChildren().clear();
        rightPane.getChildren().add(listTopicScreen.getRoot());
    }
}
