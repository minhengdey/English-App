package com.noface.englishapp.view;

import com.noface.englishapp.controller.TopicScreenController;
import com.noface.englishapp.model.Card;
import com.noface.englishapp.utils.CardCRUD;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;


public class CardTopicScreen {
    private StringProperty topic = new SimpleStringProperty();
    private MainScreen mainScreen;
    private FXMLLoader loader;

    public CardTopicScreen(TopicScreenController topicScreenController) throws IOException {
        this.mainScreen = mainScreen;
        loader = new FXMLLoader(this.getClass().getResource("CardTopicScreen.fxml"));
        loader.setController(this);
        loader.load();
        configureBinding(topicScreenController);
        configureScreenComponentEventHandler(topicScreenController);
    }
    private final ListProperty<Card> cardData = new SimpleListProperty<>(FXCollections.observableArrayList());

    private void configureBinding(TopicScreenController controller) {
        cardData.bind(controller.cardsProperty());
        topic.bind(controller.topicProperty());
    }

    public <T> T getRoot() {
        return loader.getRoot();
    }


    @FXML
    private TextField cardSearchBox;
    @FXML
    private TableView<Card> cardsTable;
    @FXML
    private SplitPane splitPane;
    @FXML
    private VBox rightPane;
    @FXML
    private Button addCardButton;
    @FXML
    private Button removeCardButton;
    @FXML
    private Button backButton;
    private CardEditingScreen cardEditingScreen;

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

    @FXML
    void initialize() throws IOException {

        cardEditingScreen = new CardEditingScreen();
        cardEditingScreen.setCardTopicEditable(false);

        HBox.setHgrow(cardSearchBox, Priority.ALWAYS);
        cardSearchBox.setMaxWidth(Double.MAX_VALUE);
        rightPane.getChildren().add(cardEditingScreen.getRoot());
        cardsTable.getColumns().clear();
        TableColumn<Card, String> cardNameColumn = new TableColumn("Card name");
        cardNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Card, String> dueTimeColumn = new TableColumn<>("Due Time");
        dueTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dueTime"));

        cardsTable.getColumns().addAll(cardNameColumn, dueTimeColumn);
        cardNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        cardsTable.setItems(cardData);
    }
    public void configureScreenComponentEventHandler(TopicScreenController topicScreenController){
        addCardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Add card button clicked");
                handleAddCardButtonClicked(event, topicScreenController);
            }
        });
        removeCardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleRemoveCardButtonClicked(event, topicScreenController);
            }
        });
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleBackButtonClicked(event, topicScreenController);
                topicScreenController.refreshListTopicTitlesList();
            }
        });


        cardsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            System.out.println("selection model change");
            if(oldSelection != null){
                if(oldSelection.getId() != null){
                    topicScreenController.saveEditedCardToDatabse(oldSelection);
                }
            }
            if (newSelection != null) {
                if (oldSelection != null){
                    oldSelection.unbind();
                }
                System.out.println("Connected card to editing screen");
                cardEditingScreen.connect(newSelection);
            }
        });
    }

    public void handleBackButtonClicked(ActionEvent event, TopicScreenController topicScreenController)  {
        Card selectCard = cardsTable.getSelectionModel().getSelectedItem();
        if(selectCard != null){
            topicScreenController.saveEditedCardToDatabse(selectCard);
        }
        mainScreen.changeToListTopicPane();

    }


    public void handleAddCardButtonClicked(ActionEvent event, TopicScreenController topicScreenController) {
        Card card = new Card("new card", "This is front content", "This is back content", topic.get(), LocalDateTime.now().toString());
        cardEditingScreen.connect(card);

        addCardButton.setDisable(true);
        cardsTable.setDisable(true);
        backButton.setDisable(true);
        removeCardButton.setDisable(true);

        VBox pane = ((VBox) cardEditingScreen.getRoot());
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        HBox bottomBar = new HBox();
        bottomBar.setSpacing(10);
        bottomBar.setAlignment(Pos.CENTER);
        pane.getChildren().add(bottomBar);
        bottomBar.getChildren().add(saveButton);
        bottomBar.getChildren().add(cancelButton);
        Pane root = (Pane) this.getRoot();

        ChangeListener<Parent> changeScreenListener = new ChangeListener<Parent>() {
            @Override
            public void changed(ObservableValue<? extends Parent> observableValue, Parent oldParent, Parent newParent) {
                if (newParent == null) {
                    cancelButton.fire();
                }
            }
        };
        root.parentProperty().addListener(changeScreenListener);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int status = topicScreenController.addCardToDatabase(card);
                if(status == CardCRUD.CARD_ADDED_SUCCESS){
                    pane.getChildren().remove(bottomBar);
                    addCardButton.setDisable(false);
                    cardsTable.setDisable(false);
                    backButton.setDisable(false);
                    removeCardButton.setDisable(false);
                    root.parentProperty().removeListener(changeScreenListener);
                }
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pane.getChildren().remove(bottomBar);
                addCardButton.setDisable(false);
                cardsTable.setDisable(false);
                backButton.setDisable(false);
                removeCardButton.setDisable(false);
                root.parentProperty().removeListener(changeScreenListener);
            }
        });
    }


    public Card handleRemoveCardButtonClicked(ActionEvent event, TopicScreenController topicScreenController) {
        event.consume();
        Card selectedCard = cardsTable.getSelectionModel().getSelectedItem();
        if(selectedCard != null){
            cardsTable.getSelectionModel().select(null);
            topicScreenController.removeCardFromDatabase(selectedCard);
            topicScreenController.refreshCardInTopic(topic.get());
            cardsTable.getSelectionModel().select(cardData.size() - 1);
        }
        return selectedCard;
    }

}
