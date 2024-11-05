package com.noface.demo.card;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;

import java.util.ArrayList;
import java.util.List;

public class CardController {
    private List<Card> cards;
    private Card card;
    private CardLearningUI cardLearningUI;
    private CardInteractor interactor;

    public CardController(FXMLLoader loader) {
        cards = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Card card = new Card(String.valueOf(i + 1), "Front side" + i, "Back side" + i);
            cards.add(card);
        }
        this.card = cards.get(0);
        this.cardLearningUI = (CardLearningUI) loader.getController();
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
        }else{
            interactor.cardAvailabledProperty().set(true);
        }
    }
    public boolean changeToNextCard() {
        try{
            System.out.println("In change card " + card);
            this.card = cards.get(cards.indexOf(card) + 1);
            interactor.setCard(card);
            return true;
        }catch(IndexOutOfBoundsException e){
            cardLearningUI.clearContentUI();
            return false;
        }
    }
}
