package com.noface.demo.screen;

import com.noface.demo.controller.TopicScreenController;
import com.noface.demo.model.Card;
import com.noface.demo.model.CardCRUD;
import com.noface.demo.resource.ResourceLoader;
import com.noface.demo.resource.Utilities;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
        cardData.bindBidirectional(controller.cardsProperty());
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
                handleAddCardButtonClicked(event);
            }
        });
        removeCardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Card cardToRemove = handleRemoveCardButtonClicked(event);
                topicScreenController.removeCardInDatabase(cardToRemove);
            }
        });
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleBackButtonClicked(event);
                topicScreenController.saveDataToDatabase();
            }
        });


        cardsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(oldSelection != null){
                int status = ResourceLoader.getInstance().getCardCRUD().addCard(oldSelection.getFrontContent(), oldSelection.getBackContent(), oldSelection.getTopic(), oldSelection.getName());
                if(status == CardCRUD.CARD_IS_AVAIALABLED){
                    Utilities.getInstance().showAlert("Card đã tồn tại", Alert.AlertType.WARNING);
                    cardData.remove(oldSelection);
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


    public void handleBackButtonClicked(ActionEvent event)  {
        mainScreen.changeToListTopicPane();

    }


    public void handleAddCardButtonClicked(ActionEvent event) {
        Card card = new Card(0, "new card", "This is front content", "This is back content", topic.get(), LocalDateTime.now().toString());
        cardData.add(card);
        cardsTable.getSelectionModel().select(cardData.getSize() - 1);
        System.out.println(cardData.get().size());
    }


    public Card handleRemoveCardButtonClicked(ActionEvent event) {
        Card selectedCard = cardsTable.getSelectionModel().getSelectedItem();
        cardData.remove(selectedCard);
        ResourceLoader.getInstance().getCardCRUD().deleteCard(selectedCard);
        return selectedCard;
    }

}
