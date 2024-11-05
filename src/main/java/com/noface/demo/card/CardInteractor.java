package com.noface.demo.card;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.time.LocalDateTime;

public class CardInteractor {
    private Scene previousScene;
    private Parent currentRoot;
    private Card card;
    private CardLearningUI cardLearningUI;
    private BooleanProperty cardAvailabled = new SimpleBooleanProperty();
    public CardInteractor(Card card, CardLearningUI cardLearningUI) {
        this.card = card;
        this.cardLearningUI = cardLearningUI;
    }

    public void setCard(Card card) {
        this.card = card;
        if(card == null) {
            cardAvailabled.set(false);
        }else{
            cardAvailabled.set(true);
            cardLearningUI.setup(card, this);
        }

    }
    public void showBackSide(){
        card.setBackCardShowed(true);
    }
    public void hideBackSide(){
        card.setBackCardShowed(false);
    }
    public void addCardTime(Long seconds){
        card.setDueTime((LocalDateTime.parse(card.getDueTime()).plusSeconds(seconds)).toString());
        System.out.println("In add card time" + card);
    }
    public Scene changeToEditCardUI(Scene currentScene) throws IOException, InterruptedException {
        this.previousScene = currentScene;
        FXMLLoader loader = new FXMLLoader(CardEditingUI.class.getResource("CardEditingUI.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        CardEditingUI cardEditingUI = (CardEditingUI) loader.getController();
        cardEditingUI.setup(card,this);
        return scene;
    }
    public Scene changeToPreviousScene(Scene currentScene){
        Scene newPreviousScene = this.previousScene;
        this.previousScene = currentScene;
        return newPreviousScene;
    }

    public void setCardFrontContent(String htmlText) {
        card.setFrontContent(htmlText);
    }
    public void setCardBackContent(String htmlText){
        card.setBackContent(htmlText);
    }

    public boolean getCardAvailabled() {
        return cardAvailabled.get();
    }

    public BooleanProperty cardAvailabledProperty() {
        return cardAvailabled;
    }
}
