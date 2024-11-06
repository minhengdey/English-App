package com.noface.demo.card;

import java.time.LocalDateTime;
import java.util.Comparator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Card{
    private final StringProperty id;
    private StringProperty dueTime;
    private final StringProperty frontContent;
    private final StringProperty backContent;

    
    public Card(String id, String frontContent, String backContent){
        this.frontContent = new SimpleStringProperty(frontContent);
        this.backContent = new SimpleStringProperty(backContent);
        this.dueTime = new SimpleStringProperty();

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
                '}';
    }
    public static Comparator<Card> comparatorByDueTime(){
        return new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return LocalDateTime.parse(
                        o1.dueTime.get()).compareTo(LocalDateTime.parse(o2.dueTime.get()));
            }
        };
    }
}
