package com.noface.demo.screen;

import com.noface.demo.card.Card;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;


public class CardTopicScreen extends Screen {
    private String topic;
    private MainScreen mainScreen;

    public CardTopicScreen(MainScreen mainScreen) throws IOException {
        this.mainScreen = mainScreen;
        loader = new FXMLLoader(this.getClass().getResource("CardTopicScreen.fxml"));
        loader.setController(this);
        loader.load();
        configureScreenComponentEventHandler();
    }
    @Override
    public <T> T getRoot() {
        return loader.getRoot();
    }

    public CardTopicScreen(MainScreen mainScreen, List<Card> cards) throws IOException {
        this(mainScreen);
        connect(cards);
        configureScreenComponentEventHandler();
    }
    public void connect(List<Card> cards){
        this.topic = cards.get(0).getTopic();
        cardData.clear();
        cardData.addAll(cards);
    }


    private final ListProperty<Card> cardData = new SimpleListProperty<>(FXCollections.observableArrayList());


    @FXML
    private ComboBox cardSearchBox;
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


    @FXML
    void initialize() throws IOException {

        cardEditingScreen = new CardEditingScreen();
        cardEditingScreen.setCardTopicEditable(true);

        HBox.setHgrow(cardSearchBox, Priority.ALWAYS);
        cardSearchBox.setMaxWidth(Double.MAX_VALUE);
        rightPane.getChildren().add(cardEditingScreen.getRoot());

        TableColumn<Card, String> cardNameColumn = new TableColumn("Card name");
        cardNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Card, String> dueTimeColumn = new TableColumn<>("Due Time");
        dueTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dueTime"));

        cardsTable.getColumns().addAll(cardNameColumn, dueTimeColumn);
        cardNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());


        cardsTable.setItems(cardData);
        cardsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (oldSelection != null) oldSelection.unbind();
                System.out.println("Connected card to editing screen");
                cardEditingScreen.connect(newSelection);
            }
        });


    }
    public void configureScreenComponentEventHandler(){
        addCardButton.setOnAction(addCardEventHandler());
        removeCardButton.setOnAction(removeCardEventHandler());
        backButton.setOnAction(backButtonClickedEventHanlder());
    }

    private EventHandler<ActionEvent> backButtonClickedEventHanlder() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainScreen.changeToListTopicScreen();
            }
        };
    }

    public EventHandler<ActionEvent> addCardEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Card card = new Card("new card", "This is front content", "This is back content", topic);
                cardData.add(card);
                cardsTable.getSelectionModel().select(cardData.getSize() - 1);
                System.out.println(cardData.get().size());
            }
        };
    }

    public EventHandler<ActionEvent> removeCardEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Card selectedCard = cardsTable.getSelectionModel().getSelectedItem();
                cardData.remove(selectedCard);
            }
        };
    }

}
