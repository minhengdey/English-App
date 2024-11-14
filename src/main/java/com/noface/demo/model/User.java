package com.noface.demo.model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class User {
    private StringProperty username, password, gender, name, phone, dob, email;

    public User() {
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        gender = new SimpleStringProperty();
        name = new SimpleStringProperty();
        phone = new SimpleStringProperty();
        dob = new SimpleStringProperty();
        email = new SimpleStringProperty();
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public String getDob() {
        return dob.get();
    }

    public StringProperty dobProperty() {
        return dob;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void bind(User o){
        username.bind(o.username);
        password.bind(o.password);
        gender.bind(o.gender);
        name.bind(o.name);
        phone.bind(o.phone);
        dob.bind(dob);
        email.bind(dob);
    }
    public void unbind(User o){
        username.unbind();
        password.unbind();
        gender.unbind();
        name.unbind();
        phone.unbind();
        dob.unbind();
        email.unbind();
    }

}
