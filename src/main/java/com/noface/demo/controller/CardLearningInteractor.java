package com.noface.demo.controller;

import com.noface.demo.card.Card;
import com.noface.demo.resource.ResourceLoader;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.PriorityQueue;
import java.util.Queue;

public class CardLearningInteractor {
    private final StringProperty backContentProperty = new SimpleStringProperty();
    private final StringProperty frontContentProperty = new SimpleStringProperty();
    private final ListProperty<Card> cardListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final BooleanProperty cardAvailabled = new SimpleBooleanProperty();
    private Queue<Card> cards = new PriorityQueue<>(Card.comparatorByDueTimeNearest());
    private Card currentCard;
    public CardLearningInteractor() {
    }
    public void getDataFromDatabase(){
        cardListProperty.addAll(ResourceLoader.getInstance().getCardsSampleData());
    }

    public void load(){
        getDataFromDatabase();
        cards.addAll(cardListProperty.get());
        changeToNextCard();
    }
    public void  changeToNextCard(){
        removeInvalid();
        Card previousCard = currentCard;
        if(cards.size() != 0){
            cardAvailabledProperty().set(true);
            currentCard = cards.poll();
            frontContentProperty.set(currentCard.getFrontContent());
            backContentProperty.set(currentCard.getBackContent());
            if(previousCard != null && LocalDateTime.parse(currentCard.getDueTime()).compareTo(
                    LocalDate.now().plusDays(1).atStartOfDay()) < 0){
                cards.add(previousCard);
            }
        }else{
            if(previousCard != null && LocalDateTime.parse(currentCard.getDueTime()).compareTo(
                    LocalDate.now().plusDays(1).atStartOfDay()) < 0){
                cards.add(previousCard);
            }
            if(cards.size() == 0){
                cardAvailabledProperty().set(false);
            }else{
                currentCard = cards.poll();
                frontContentProperty.set(currentCard.getFrontContent());
                backContentProperty.set(currentCard.getBackContent());
            }
        }
    }
    public void removeInvalid(){
        while(true){
            if(cards.size() == 0){
                break;
            }else{
                Card card = cards.peek();
                if(LocalDateTime.parse(card.getDueTime()).compareTo(
                        LocalDate.now().plusDays(1).atStartOfDay()) >= 0){
                    cards.remove();
                }else{
                    break;
                }
            }
        }
    }
    public void plusCardDueTime(Long seconds){
        currentCard.setDueTime((LocalDateTime.now().plusSeconds(seconds)).toString());
        System.out.println("In add card time " + currentCard);
    }

    public ObservableList<Card> getCardListProperty() {
        return cardListProperty.get();
    }

    public ListProperty<Card> cardListPropertyProperty() {
        return cardListProperty;
    }


    public String getBackContentProperty() {
        return backContentProperty.get();
    }

    public StringProperty backContentPropertyProperty() {
        return backContentProperty;
    }

    public String getFrontContentProperty() {
        return frontContentProperty.get();
    }

    public StringProperty frontContentPropertyProperty() {
        return frontContentProperty;
    }

    public boolean isCardAvailabled() {
        return cardAvailabled.get();
    }

    public BooleanProperty cardAvailabledProperty() {
        return cardAvailabled;
    }

    public Card getCurrentCard() {
        return currentCard;
    }
}
