package com.noface.englishapp.utils;

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

    private String normalize_name(String s)
    {
        String[] k = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String i : k)
        {
            sb.append(i.substring(0, 1).toUpperCase()).append(i.substring(1).toLowerCase()).append(" ");
        }
        return sb.toString().trim();
    }

    public CardRequest(String frontSide, String backSide, String topic, String date, String name)
    {
        this.frontSide = new SimpleStringProperty(frontSide);
        this.backSide = new SimpleStringProperty(backSide);
        this.topic = new SimpleStringProperty(normalize_name(topic));
        this.date = new SimpleStringProperty(normalize_name(date));
        this.name = new SimpleStringProperty(normalize_name(name));
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
