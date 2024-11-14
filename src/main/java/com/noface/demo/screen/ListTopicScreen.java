package com.noface.demo.screen;

import com.noface.demo.card.Card;
import com.noface.demo.controller.TopicScreenController;
import com.noface.demo.controller.CardLearningController;
import com.noface.demo.screen.component.TopicBar;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ListTopicScreen {
    private  FXMLLoader loader;
    private MainScreen mainScreen;
    private ListProperty<String> topicTitles =  new SimpleListProperty<>();

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

    public ListTopicScreen(TopicScreenController controller) throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("ListTopicScreen.fxml"));
        loader.setController(this);
        loader.load();
        topicTitles.bind(controller.topicTitlesProperty());
        configureScreenComponent();
    }
    public void configureScreenComponent(){
        addTopicButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    handleAddTopicButtonClicked(actionEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        topicTitles.addListener(new ChangeListener<ObservableList<String>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<String>> observable, ObservableList<String> oldValue, ObservableList<String> newValue) {
                addTopicBarToScreen();
            }
        });
    }

    private void handleAddTopicButtonClicked(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Card card = new Card("new name", "new name", "new name", "new name");
        CardEditingScreen screen = new CardEditingScreen();
        stage.setScene(new Scene(screen.getRoot()));
        stage.show();
    }

    public <T> T getRoot() {
        return loader.getRoot();
    }

    @FXML
    private VBox topicBarPane;


    public void addTopicBarToScreen(){
        topicBarPane.getChildren().clear();
        for(String title : topicTitles.get()){
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
                try {
                    CardLearningController controller = new CardLearningController();

                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(controller.getScreen().getRoot()));
                    controller.load();
                    stage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private EventHandler<ActionEvent> editButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainScreen.changeToCardTopicPane();
            }
        };
    }
    @FXML
    private Button addTopicButton;
}
