package com.noface.englishapp.controller;

import com.noface.englishapp.model.Card;
import com.noface.englishapp.utils.CardCRUD;
import com.noface.englishapp.utils.ResourceLoader;
import com.noface.englishapp.utils.Utilities;
import com.noface.englishapp.view.CardTopicScreen;
import com.noface.englishapp.view.ListTopicScreen;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.IOException;

public class TopicScreenController {
    private final StringProperty topic = new SimpleStringProperty();
    private final ListTopicScreen topicScreen;
    private final CardTopicScreen cardScreen;
    private final ListProperty<String> topicTitles = new SimpleListProperty(FXCollections.observableArrayList());
    private final ListProperty<Card> cards = new SimpleListProperty<>(FXCollections.observableArrayList());
    public TopicScreenController() throws IOException {

        topicScreen = new ListTopicScreen(this);
        cardScreen = new CardTopicScreen(this);

    }

    public void changeTopic(String newTopic){
        topic.set(newTopic);
    }
    public String getTopic() {
        return topic.get();
    }

    public StringProperty topicProperty() {
        return topic;
    }

    public CardTopicScreen getScreen() {
        return cardScreen;
    }

    public ListTopicScreen setMainScreen() {
        return topicScreen;
    }


    public ObservableList<String> getTopicTitles() {
        return topicTitles.get();
    }

    public ListProperty<String> topicTitlesProperty() {
        return topicTitles;
    }

    public ObservableList<Card> getCards() {
        return cards.get();
    }

    public ListProperty<Card> cardsProperty() {
        return cards;
    }

    public void removeCardFromDatabase(Card card){
        ResourceLoader.getInstance().getCardCRUD().deleteCard(card);
    }
    public int addCardToDatabase(Card card){

        int status = ResourceLoader.getInstance().getCardCRUD().addCard(card.getFrontContent(), card.getBackContent(), card.getTopic(), card.getName());
        if(status == CardCRUD.CARD_ADDED_SUCCESS){
            refreshCardInTopic(card.getTopic());
        }
        return status;
    }
    public void refreshListTopicTitlesList(){
        topicTitles.clear();
        topicTitles.addAll(ResourceLoader.getInstance().getCardCRUD().getAllTopics());
        topicScreen.initialize();
    }
    public void refreshCardInTopic(String topic){
        this.topic.set(topic);
        cards.clear();
        cards.setAll(ResourceLoader.getInstance().getCardCRUD().getAllCardsByTopic(topic));
    }

    public void saveEditedCardToDatabse(Card card) {
        int status = ResourceLoader.getInstance().getCardCRUD().editCard(card,
                card.getFrontContent(), card.getBackContent(), card.getTopic(), card.getName(), card.getDueTime());
        if(status == CardCRUD.ERROR){
            Utilities.getInstance().showAlert("Error occured", Alert.AlertType.WARNING);
        }
    }
    public int deleteTopic(String topic){
        this.topic.set(topic);
        int status = ResourceLoader.getInstance().getCardCRUD().deleteCardByTopic(topic);
        return status;
    }
    public int renameTopic(String oldTopic, String newTopic){
        int status = ResourceLoader.getInstance().getCardCRUD().renameTopic(oldTopic, newTopic);
        return status;
    }
}
