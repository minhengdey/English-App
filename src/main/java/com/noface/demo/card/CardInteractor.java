package com.noface.demo.card;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class CardInteractor {
    private Scene previousScene;
    private Parent currentRoot;
    private Card card;
    private CardLearningUI cardLearningUI;
    private BooleanProperty cardAvailabled = new SimpleBooleanProperty();
    public CardInteractor(Card card, CardLearningUI cardLearningUI) {
        this.card = card;
        this.cardLearningUI = cardLearningUI;
        cardLearningUI.bindInteractorProperty(this);
    }

    public void setCard(Card card) {
        this.card = card;
        if(card == null) {
            cardAvailabled.set(false);
            // controller se listen false sau do thay doi card cho phu hop
        }else{
            cardLearningUI.connect(card, this);
        }
        System.out.println("card available " + cardAvailabled.get());
    }
    public void plusCardDueTime(Long seconds){
        card.setDueTime((LocalDateTime.now().plusSeconds(seconds)).toString());
        System.out.println("In add card time " + card);
    }
    public void changeToEditCardUI(Scene currentScene) throws IOException, InterruptedException {
        if(card != null) {
            this.previousScene = currentScene;
            Stage stage = (Stage) currentScene.getWindow();
            FXMLLoader loader = new FXMLLoader(CardEditingUI.class.getResource("CardEditingUI.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            CardEditingUI cardEditingUI = loader.getController();
            cardEditingUI.connect(card,this);
        }

    }
    public void changeToPreviousScene(Scene currentScene){
        Stage stage = (Stage) currentScene.getWindow();
        stage.setScene(previousScene);
        this.previousScene = currentScene;
    }


    public boolean getCardAvailabled() {
        return cardAvailabled.get();
    }

    public BooleanProperty cardAvailabledProperty() {
        return cardAvailabled;
    }

}
