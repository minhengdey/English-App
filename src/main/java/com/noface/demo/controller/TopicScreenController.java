package com.noface.demo.controller;

import com.noface.demo.model.Card;
import com.noface.demo.resource.ResourceLoader;
import com.noface.demo.screen.CardTopicScreen;
import com.noface.demo.screen.ListTopicScreen;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

public class TopicScreenController {
    private StringProperty topic = new SimpleStringProperty();
    private ListTopicScreen topicScreen;
    private CardTopicScreen cardScreen;
    private ListProperty<String> topicTitles = new SimpleListProperty(FXCollections.observableArrayList());
    private ListProperty<Card> cards = new SimpleListProperty<>(FXCollections.observableArrayList());
    public TopicScreenController() throws IOException {

        topicScreen = new ListTopicScreen(this);
        cardScreen = new CardTopicScreen(this);
        topic.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handleTopicChange(observable, oldValue, newValue);
            }
        });

    }
    public void handleTopicChange(ObservableValue<? extends String> observable, String oldValue, String newValue){
        // SU DUNG DE LAY DATA TU DATABASE VOI TOPIC TITLES CHO TRUOC

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

    public ListTopicScreen getTopicScreen() {
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

    public void saveDataToDatabase() {
        // METHOD DUOC SU DUNG DE LUU ListProperty<Card> VAO DATABASE
    }
    public void removeCardInDatabase(Card cardToRemove){

    }
    public int addCardToDatabase(Card card){
        System.out.println(card);
        int status = ResourceLoader.getInstance().getCardCRUD().addCard(card.getFrontContent(), card.getBackContent(), card.getTopic(), card.getName());
        return status;
    }
    public void refreshListTopicTitlesList(){
        topicTitles.clear();
        topicTitles.addAll(ResourceLoader.getInstance().getCardCRUD().getAllTopics());
    }
    public void loadCardByTopic(String topic){
        cards.clear();
        cards.setAll(ResourceLoader.getInstance().getCardCRUD().getAllCardsByTopic(topic));
    }
}
