package com.noface.demo.controller;

import com.noface.demo.model.Card;
import com.noface.demo.screen.CardLearningScreen;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.io.IOException;

public class CardLearningController {
    private ListProperty<Card> cardListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    private Card card;
    private CardLearningScreen cardLearningScreen;
    private CardLearningInteractor interactor;

    public CardLearningController() throws IOException {
        interactor = new CardLearningInteractor();
        cardLearningScreen = new CardLearningScreen(interactor);

    }
    public void loadCardByTopic(String topicTitle){
        interactor.loadByTopicTitle(topicTitle);
        cardLearningScreen.startShowing();
    }
    public CardLearningScreen getScreen() {
        return cardLearningScreen;
    }
}