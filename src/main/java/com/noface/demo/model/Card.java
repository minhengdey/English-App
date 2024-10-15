package com.noface.demo.model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.util.Subscription;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Card{
    private String id;
    private ArrayList<String> fields;
    private LocalDate dueDate;

    public Card(String id, ArrayList<String> fields, LocalDate dueDate) {
        this.id = id;
        this.fields = fields;
        this.dueDate = dueDate;
    }


    public String getId() {
        return id;
    }

    public ArrayList<String> getFields() {
        return fields;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}
