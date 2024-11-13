package com.noface.demo.Controller;

import com.noface.demo.card.Card;
import com.noface.demo.screen.CardEditingScreen;
import com.noface.demo.screen.CardLearningScreen;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class CardInteractor {
    private Card card;
    private CardLearningScreen cardLearningScreen;
    private final BooleanProperty cardAvailabled = new SimpleBooleanProperty();

    private final ListProperty<Card> cardListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    private List<Card> cards;
    public CardInteractor(Card card, CardLearningScreen cardLearningScreen) {
        this.card = card;
        this.cardLearningScreen = cardLearningScreen;
    }
    public CardInteractor(Card card){
        this.card = card;
    }


    public void setCard(Card card) {
        this.card = card;
        if(card == null) {
            cardAvailabled.set(false);
            cardLearningScreen.bindInteractorProperty(this);
            // controller se listen false sau do thay doi card cho phu hop
        }else{
            cardLearningScreen.connect(card, this);
        }

        System.out.println("card available " + cardAvailabled.get());
    }
    public void plusCardDueTime(Long seconds){
        card.setDueTime((LocalDateTime.now().plusSeconds(seconds)).toString());
        System.out.println("In add card time " + card);
    }

    public ObservableList<Card> getCardListProperty() {
        return cardListProperty.get();
    }

    public ListProperty<Card> cardListPropertyProperty() {
        return cardListProperty;
    }

    public boolean getCardAvailabled() {
        return cardAvailabled.get();
    }

    public BooleanProperty cardAvailabledProperty() {
        return cardAvailabled;
    }

    public Stage openEditingStage(Stage ownerStage) throws IOException {
        CardEditingScreen screen = new CardEditingScreen();
        screen.connect(card);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(ownerStage);
        stage.setScene(new Scene(screen.getRoot()));
        stage.showAndWait();
        return stage;
    }
}
