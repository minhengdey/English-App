package com.noface.englishapp.controller;

import com.noface.englishapp.model.Card;
import com.noface.englishapp.utils.CardCRUD;
import com.noface.englishapp.utils.ResourceLoader;
import com.noface.englishapp.utils.Utilities;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

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

    public void loadByTopicTitle(String topicTitle){
        cardListProperty.clear();
        cardListProperty.setAll(ResourceLoader.getInstance().getCardCRUD().getAllCardsByTopic(topicTitle));
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
        ResourceLoader.getInstance().getCardCRUD().editCard(currentCard, currentCard.getFrontContent(), currentCard.getBackContent(),
                currentCard.getTopic(), currentCard.getName(), currentCard.getDueTime());
    }
    public void saveEditedCard(Card card){

        int status = ResourceLoader.getInstance().getCardCRUD().editCard(card, card.getFrontContent(), card.getBackContent(),
                card.getTopic(), card.getName(), card.getDueTime());
        if(status == CardCRUD.CARD_EDITED_SUCCESS){
            frontContentProperty.set(card.getFrontContent());
            backContentProperty.set(card.getBackContent());
        }else{
            Utilities.getInstance().showAlert("Lưu không thành công", Alert.AlertType.WARNING);
        }
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
