package com.noface.englishapp.view;

import com.noface.englishapp.controller.TopicScreenController;
import com.noface.englishapp.controller.CardLearningController;
import com.noface.englishapp.model.Card;
import com.noface.englishapp.utils.CardCRUD;
import com.noface.englishapp.view.component.TopicBar;
import javafx.animation.PauseTransition;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListTopicScreen {
    private  FXMLLoader loader;
    private MainScreen mainScreen;
    private ListProperty<String> topicTitles =  new SimpleListProperty<>();
    private TopicScreenController topicScreenController;
    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
    private PauseTransition pause = new PauseTransition(Duration.millis(300));

    public ListTopicScreen(TopicScreenController controller) throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("ListTopicScreen.fxml"));
        loader.setController(this);
        loader.load();
        topicTitles.bind(controller.topicTitlesProperty());
        this.topicScreenController = controller;
        configureScreenComponent(controller);
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


        filterTopicTitles.addListener(new ChangeListener<ObservableList<String>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<String>> observable, ObservableList<String> oldValue, ObservableList<String> newValue) {
                addTopicBarToScreen();
            }
        });

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleSearchButtonClicked();
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            pause.setOnFinished(event -> handleSearchButtonClicked());
            pause.playFromStart();
        });
    }



    private void handleAddTopicButtonClicked(ActionEvent actionEvent, TopicScreenController controller) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Card newCard = new Card(0, "Card #1", "This is front content", "This is back content", "Default", LocalDateTime.now().toString());
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
        for(String title : filterTopicTitles.get()){
            TopicBar topicBar = new TopicBar(title);
            topicBarPane.getChildren().add(topicBar);
            topicBar.setOnEditButtonClicked(editButtonClickedEventHandler(topicBar));
            topicBar.setOnLearnButtonClicked(learnButtonClickedEventHandler(topicBar));
            topicBar.setOnRemoveButtonClicked(removeButtonClickedEventHandler(topicBar));
            topicBar.setOnRenameButtonClicked(renameButtonClickedEventHandler(topicBar));
        }
    }

    private EventHandler renameButtonClickedEventHandler(TopicBar topicBar) {
        return new EventHandler() {
            @Override
            public void handle(Event event) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Change topic name");
                dialog.setHeaderText("Enter your new topic name");
                dialog.setContentText("Name:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {
                    int status = topicScreenController.renameTopic(topicBar.getTopicName(), name);
                    if(status == CardCRUD.TOPIC_RENAME_SUCCESS){
                        dialog.close();
                        topicBar.updateTopicTitle(CardCRUD.normalize_name(name));
                        topicScreenController.refreshListTopicTitlesList();
                    }else{
                        dialog.showAndWait();
                    }
                });
            }
        };
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
        searchButton.fire();
    }

    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    private ListProperty<String> filterTopicTitles = new SimpleListProperty<>(FXCollections.observableArrayList());
    private void handleSearchButtonClicked() {
        System.out.println("Search button clicked");
        String filter = searchField.getText();
        if(filter != ""){
            filter = CardCRUD.normalize_name(filter);
        }
        filterTopicTitles.clear();
        String finalFilter = filter;
        filterTopicTitles.addAll(
                topicTitles.get().stream()
                        .filter(word -> removeDiacritics(word).startsWith(removeDiacritics(finalFilter)))
                        .collect(Collectors.toList())
        );

    }

    public static String removeDiacritics(String input) {
        if (input == null) return "";
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "D");
    }
}
