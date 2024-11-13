package com.noface.demo.Controller;

import com.noface.demo.card.Card;
import com.noface.demo.screen.CardLearningScreen;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.*;

public class CardLearningController {
    private ListProperty<Card> cardListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    private Queue<Card> cards = new PriorityQueue<>(Card.comparatorByDueTimeNearest());
    private Card card;
    private CardLearningScreen cardLearningScreen;
    private CardLearningInteractor interactor;

    public CardLearningController() throws IOException {
        interactor = new CardLearningInteractor();
        cardLearningScreen = new CardLearningScreen(interactor);

    }
    public void load(){
        interactor.load();
        cardLearningScreen.startShowing();
    }
    public CardLearningScreen getCardLearningScreen() {
        return cardLearningScreen;
    }
}
