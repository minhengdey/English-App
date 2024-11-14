package com.noface.demo.card;

import com.fasterxml.jackson.annotation.JsonInclude;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CardRequest
{
    private StringProperty frontSide, backSide, topic, date, name;

    public CardRequest()
    {
        this.frontSide = new SimpleStringProperty();
        this.backSide = new SimpleStringProperty();
        this.topic = new SimpleStringProperty();
        this.date = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
    }

    public CardRequest(String frontSide, String backSide, String topic, String date, String name)
    {
        this.frontSide = new SimpleStringProperty(frontSide);
        this.backSide = new SimpleStringProperty(backSide);
        this.topic = new SimpleStringProperty(topic);
        this.date = new SimpleStringProperty(date);
        this.name = new SimpleStringProperty(name);
    }

    public String getfrontSide()
    {
        return frontSide.get();
    }

    public void setFrontSide(String frontSide)
    {
        this.frontSide.set(frontSide);
    }

    public StringProperty frontSideProperty()
    {
        return frontSide;
    }

    public String getBackSide()
    {
        return backSide.get();
    }

    public void setBackSide(String backSide)
    {
        this.backSide.set(backSide);
    }

    public StringProperty backSideProperty()
    {
        return backSide;
    }

    public String getTopic()
    {
        return topic.get();
    }

    public void setTopic(String topic)
    {
        this.topic.set(topic);
    }

    public StringProperty topicProperty()
    {
        return topic;
    }

    public String getDate()
    {
        return date.get();
    }

    public void setDate(String date)
    {
        this.date.set(date);
    }

    public StringProperty dateProperty()
    {
        return date;
    }

    public String getName()
    {
        return name.get();
    }

    public void setName(String name)
    {
        this.name.set(name);
    }

    public StringProperty nameProperty()
    {
        return name;
    }
}
