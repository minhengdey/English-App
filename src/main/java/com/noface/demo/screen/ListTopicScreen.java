package com.noface.demo.screen;

import com.noface.demo.controller.TopicScreenController;
import com.noface.demo.controller.CardLearningController;
import com.noface.demo.model.Card;
import com.noface.demo.model.CardCRUD;
import com.noface.demo.resource.Utilities;
import com.noface.demo.screen.component.TopicBar;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class ListTopicScreen {
    private  FXMLLoader loader;
    private MainScreen mainScreen;
    private ListProperty<String> topicTitles =  new SimpleListProperty<>();
    private TopicScreenController topicScreenController;
    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

    public ListTopicScreen(TopicScreenController controller) throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("ListTopicScreen.fxml"));
        loader.setController(this);
        loader.load();
        topicTitles.bind(controller.topicTitlesProperty());
        this.topicScreenController = controller;
        configureScreenComponent(controller);
        controller.refreshListTopicTitlesList();
    }
    public void configureScreenComponent(TopicScreenController controller){
        addTopicButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    handleAddTopicButtonClicked(actionEvent, controller);
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

    private void handleAddTopicButtonClicked(ActionEvent actionEvent, TopicScreenController controller) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Card newCard = new Card(0, "new name", "new name", "new name", "new name", LocalDateTime.now().toString());
        CardEditingScreen screen = new CardEditingScreen();
        screen.connect(newCard);
        VBox root = screen.getRoot();
        HBox bottomBar = new HBox();
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
;
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int status = controller.addCardToDatabase(newCard);
                if(status == CardCRUD.CARD_IS_AVAILABLED){
                    Utilities.getInstance().showAlert("Card đã tồn tại", Alert.AlertType.WARNING);
                }
                if(status == CardCRUD.CARD_ADDED_SUCCESS){
                    controller.refreshListTopicTitlesList();
                    stage.close();
                }

            }
        });

        bottomBar.getChildren().addAll(saveButton, cancelButton);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setSpacing(10);
        root.getChildren().add(bottomBar);


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
            topicBar.setOnEditButtonClicked(editButtonClickedEventHandler(topicBar));
            topicBar.setOnLearnButtonClicked(learnButtonClickedEventHandler(topicBar));
            topicBar.setOnRemoveButtonClicked(removeButtonClickedEventHandler(topicBar));
        }
    }

    private EventHandler removeButtonClickedEventHandler(TopicBar topicBar) {
        return new EventHandler() {
            @Override
            public void handle(Event event) {
                int status = topicScreenController.deleteTopic(topicBar.getTopicName());
                if(status == CardCRUD.TOPIC_DELETED_SUCCESS){
                    topicBarPane.getChildren().remove(topicBar);
                }
            }
        };
    }

    private EventHandler<ActionEvent> learnButtonClickedEventHandler(TopicBar topicBar) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    CardLearningController controller = new CardLearningController();
                    controller.loadCardByTopic(topicBar.getTopicName());
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(controller.getScreen().getRoot()));

                    stage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private EventHandler<ActionEvent> editButtonClickedEventHandler(TopicBar topicBar) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainScreen.changeToCardTopicPane(topicBar.getTopicName());
            }
        };
    }
    @FXML
    private Button addTopicButton;
    @FXML
    public void initialize(){
        topicBarPane.getChildren().clear();
    }
}
