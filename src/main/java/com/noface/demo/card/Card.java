package com.noface.demo.card;

import java.time.LocalDateTime;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Card{
    private final StringProperty id;
    private StringProperty dueTime;
    private final StringProperty frontContent;
    private final StringProperty backContent;
    private final BooleanProperty backCardShowed;
    
    public Card(String id, String frontContent, String backContent){
        this.frontContent = new SimpleStringProperty(frontContent);
        this.backContent = new SimpleStringProperty(backContent);
        this.dueTime = new SimpleStringProperty();
        this.backCardShowed = new SimpleBooleanProperty(false);
        this.id = new SimpleStringProperty(id);
        this.dueTime = new SimpleStringProperty(LocalDateTime.now().toString());
    }

    public String getDueTime(){
        return dueTime.get();
    }

    public StringProperty frontContentProperty() {
        return frontContent;
    }

    public StringProperty backContentProperty() {
        return backContent;
    }

    public BooleanProperty backCardShowedProperty() {
        return backCardShowed;
    }

    public void setBackCardShowed(boolean backCardShowed) {
        this.backCardShowed.set(backCardShowed);
    }

    public void setDueTime(String dueTime) {
        this.dueTime.set(dueTime);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", dueTime=" + dueTime +
                ", frontContent=" + frontContent +
                ", backContent=" + backContent +
                ", backCardShowed=" + backCardShowed +
                '}';
    }
}
