package com.example.englishapp;

import com.fasterxml.jackson.annotation.JsonInclude;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Vocabulary
{
    private long id;
    private StringProperty english, vietnamese;

    public Vocabulary()
    {
        this.english = new SimpleStringProperty();
        this.vietnamese = new SimpleStringProperty();
    }

    public Vocabulary(String english, String vietnamese)
    {
        this.english = new SimpleStringProperty(english);
        this.vietnamese = new SimpleStringProperty(vietnamese);
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getEnglish()
    {
        return english.get();
    }

    public void setEnglish(String english)
    {
        this.english.set(english);
    }

    public StringProperty EnglishProperty()
    {
        return english;
    }

    public String getVietnamese()
    {
        return vietnamese.get();
    }

    public void setVietnamese(String vietnamese)
    {
        this.vietnamese.set(vietnamese);
    }

    public StringProperty VietnameseProperty()
    {
        return vietnamese;
    }
}
