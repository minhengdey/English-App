package com.noface.demo.card;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Card{
    private StringProperty id;
    private StringProperty dueTime;
    private StringProperty frontContent;
    private StringProperty backContent;
    private BooleanProperty backCardShowed;
    
    public Card(String id, String frontContent, String backContent){
        this.frontContent = new SimpleStringProperty(frontContent);
        this.backContent = new SimpleStringProperty(backContent);
        this.dueTime = new SimpleStringProperty();
        this.backCardShowed = new SimpleBooleanProperty(false);
        this.id = new SimpleStringProperty(id);
        this.dueTime = new SimpleStringProperty(LocalDateTime.now().toString());
    }

    public String getId(){
        return id.get();
    }
    public String getDueTime(){
        return dueTime.get();
    }
    public String getFrontContent(){
        return frontContent.get();
    }
    public String getBackContent(){
        return backContent.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public StringProperty dueTimeProperty() {
        return dueTime;
    }

    public StringProperty frontContentProperty() {
        return frontContent;
    }

    public StringProperty backContentProperty() {
        return backContent;
    }

    public boolean isBackCardShowed() {
        return backCardShowed.get();
    }

    public BooleanProperty backCardShowedProperty() {
        return backCardShowed;
    }

    public void setBackContent(String backContent) {
        this.backContent.set(backContent);
    }

    public void setFrontContent(String frontContent) {
        this.frontContent.set(frontContent);
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
