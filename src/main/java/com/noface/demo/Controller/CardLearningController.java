package com.noface.demo.Controller;

import com.noface.demo.card.Card;
import com.noface.demo.resource.ResourceLoader;
import com.noface.demo.screen.CardLearningScreen;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class CardLearningController {
    private ListProperty<Card> cardListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    private Queue<Card> cards = new PriorityQueue<>(Card.comparatorByDueTimeNearest());
    private Card card;
    private CardLearningScreen cardLearningScreen;
    private CardInteractor interactor;

    public CardLearningController() throws IOException {
        card = new Card();
        cardLearningScreen = new CardLearningScreen();
        interactor = new CardInteractor(card);
        cardListProperty.bind(interactor.cardListPropertyProperty());
    }
    public void connect(List<Card> cardsToAdd){
        this.cards.addAll(cardsToAdd);
        removeInvalidCard();
        this.card = cards.poll();

        this.cardLearningScreen =  cardLearningScreen;
        interactor = new CardInteractor(card, cardLearningScreen);
        interactor.setCard(card);

        interactor.cardAvailabledProperty().addListener((observable, oldValue, newValue) -> {
            handleCardAvailabledPropertyChange(observable, oldValue, newValue);
        });
        handleCardAvailabledPropertyChange(interactor.cardAvailabledProperty(), interactor.getCardAvailabled(),
                interactor.getCardAvailabled());
        if(cards.size() == 0){
            System.out.println("size = 0");
            interactor.cardAvailabledProperty().set(false);
        }

    }
    public void handleCardAvailabledPropertyChange(Observable observable, boolean oldValue, boolean newValue){
        System.out.println("run in controller " + newValue);
        if(newValue == false){
            interactor.cardAvailabledProperty().set(changeToNextCard());
        }
    }
    public void removeInvalidCard(){
        while(true){
            if(cards.size() == 0) break;
            else{
                if( LocalDateTime.parse(cards.peek().getDueTime()).compareTo(LocalDate.now().atStartOfDay().plusDays(1l)) >= 0) {
                    cards.poll();
                }else{
                    break;
                }
            }
        }
    }
    public boolean changeToNextCard() {
        System.out.println("In change card " + card);
        Card previousCard = this.card;
        this.card = cards.poll();
        if(previousCard != null && LocalDateTime.parse(previousCard.getDueTime()).compareTo(LocalDate.now().atStartOfDay().plusDays(1l)) < 0){
            cards.add(previousCard);
        }
        if(this.card == null){
            if(cards.peek() != null){
                this.card = cards.poll();
                interactor.setCard(card);
                return true;
            }
            return false;
        }
        interactor.setCard(card);
        return true;
    }
    public void scrapeData(){
        cards.addAll(ResourceLoader.getInstance().getCardsSampleData());
    }

    public CardLearningScreen getCardLearningScreen() {
        return cardLearningScreen;
    }
}
