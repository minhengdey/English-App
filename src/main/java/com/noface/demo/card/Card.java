package com.noface.demo.card;

import java.time.LocalDateTime;
import java.util.Comparator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Card{
    private final StringProperty name;
    private final StringProperty id;
    private final StringProperty dueTime;
    private final StringProperty frontContent;
    private final StringProperty backContent;
    private final StringProperty topic;



    
    public Card(String name, String frontContent, String backContent, String topic){
        this.name = new SimpleStringProperty(name);
        this.frontContent = new SimpleStringProperty(frontContent);
        this.backContent = new SimpleStringProperty(backContent);
        this.dueTime = new SimpleStringProperty(LocalDateTime.now().toString());
        this.id = new SimpleStringProperty();
        this.topic = new SimpleStringProperty(topic);
    }
    public void unbind(){
        name.unbind();
        dueTime.unbind();
        frontContent.unbind();
        backContent.unbind();
    }


    @Override
    public String toString() {
        return "Card{" +
                "name=" + name +
                ", id=" + id +
                ", dueTime=" + dueTime +
                ", frontContent=" + frontContent +
                ", backContent=" + backContent +
                ", topic=" + topic +
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

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getDueTime() {
        return dueTime.get();
    }

    public StringProperty dueTimeProperty() {
        return dueTime;
    }

    public String getFrontContent() {
        return frontContent.get();
    }

    public StringProperty frontContentProperty() {
        return frontContent;
    }

    public String getBackContent() {
        return backContent.get();
    }

    public StringProperty backContentProperty() {
        return backContent;
    }
    public void setDueTime(String dueTime){
        this.dueTime.set(dueTime);
    }

    public String getTopic() {
        return topic.get();
    }

    public StringProperty topicProperty() {
        return topic;
    }
}
