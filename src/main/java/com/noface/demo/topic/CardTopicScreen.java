package com.noface.demo.topic;

import com.noface.demo.card.Card;
import com.noface.demo.card.CardEditingScreen;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CardTopicScreen {
    private final FXMLLoader loader;
    private String topic;

    public CardTopicScreen() throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("CardTopicScreen.fxml"));
        loader.setController(this);
        loader.load();
    }

    public <T> T getRoot() {
        return loader.getRoot();
    }

    public CardTopicScreen(List<Card> cards) throws IOException {
        this();
        this.topic = cards.get(0).getTopic();
        cardData.addAll(cards);
        System.out.println(cardData);
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

        addCardButton.setOnAction(addCardEventHandler());
        removeCardButton.setOnAction(removeCardEventHandler());
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
