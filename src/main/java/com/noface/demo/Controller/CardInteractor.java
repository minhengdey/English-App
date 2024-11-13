package com.noface.demo.Controller;

import com.noface.demo.card.Card;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class CardInteractor {
    private ListProperty<Card> cards = new SimpleListProperty<>(FXCollections.observableArrayList());
    private StringProperty frontContent = new SimpleStringProperty();
    private StringProperty backContent = new SimpleStringProperty();
    private StringProperty cardName = new SimpleStringProperty();
    public void getCardByTopic(String topic){

    }
}
