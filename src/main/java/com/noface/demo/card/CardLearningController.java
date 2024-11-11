package com.noface.demo.card;

import com.noface.demo.resource.ResourceLoader;
import javafx.beans.Observable;
import javafx.fxml.FXMLLoader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class CardLearningController {
    private Queue<Card> cards = new PriorityQueue<>(Card.comparatorByDueTime());
    private Card card;
    private CardLearningUI cardLearningUI;
    private CardInteractor interactor;

    public CardLearningController(FXMLLoader loader) {
        scrapeData();
        this.card = cards.poll();
        this.cardLearningUI =  loader.getController();

        interactor = new CardInteractor(card, cardLearningUI);

        interactor.setCard(card);
        interactor.cardAvailabledProperty().addListener((observable, oldValue, newValue) -> {
            handleCardAvailabledPropertyChange(observable, oldValue, newValue);
        });
        handleCardAvailabledPropertyChange(interactor.cardAvailabledProperty(), interactor.getCardAvailabled(),
                interactor.getCardAvailabled());
    }
    public void handleCardAvailabledPropertyChange(Observable observable, boolean oldValue, boolean newValue){
        System.out.println("run in controller " + newValue);
        if(newValue == false){
            interactor.cardAvailabledProperty().set(changeToNextCard());
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
}
