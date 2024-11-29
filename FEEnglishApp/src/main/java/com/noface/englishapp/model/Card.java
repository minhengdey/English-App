package com.noface.englishapp.model;

import java.time.LocalDateTime;
import java.util.Comparator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Card{
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty dueTime = new SimpleStringProperty();
    private final StringProperty frontContent = new SimpleStringProperty();
    private final StringProperty backContent = new SimpleStringProperty();
    private final StringProperty topic = new SimpleStringProperty();



    
    public Card(long id, String name, String frontContent, String backContent, String topic, String dueTime){
        this(name, frontContent, backContent, topic, dueTime);
        this.id.set(Long.toString(id));
    }

    public Card(String name, String frontContent, String backContent, String topic, String dueTime){
        this.name.set(name);
        this.frontContent.set(frontContent);
        this.backContent.set(backContent);
        this.dueTime.set(dueTime);
        this.topic.set(topic);
    }

    public Card() {
    }

    public void unbind(){
        name.unbind();
        dueTime.unbind();
        frontContent.unbind();
        backContent.unbind();
        topic.unbind();

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

    public static Comparator<Card> comparatorByDueTimeNearest(){
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
