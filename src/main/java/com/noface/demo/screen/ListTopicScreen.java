package com.noface.demo.screen;

import com.noface.demo.Controller.TopicScreenController;
import com.noface.demo.Controller.CardLearningController;
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
import javafx.scene.layout.VBox;
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
        topicTitles.addListener(new ChangeListener<ObservableList<String>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<String>> observable, ObservableList<String> oldValue, ObservableList<String> newValue) {
                addTopicBarToScreen();
            }
        });
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
                    stage.setScene(new Scene(controller.getCardLearningScreen().getRoot()));
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
}
