package com.noface.englishapp.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private StringProperty username, gender, name, phone, dob, email;

    public User() {
        username = new SimpleStringProperty();
        gender = new SimpleStringProperty();
        name = new SimpleStringProperty();
        phone = new SimpleStringProperty();
        dob = new SimpleStringProperty();
        email = new SimpleStringProperty();
    }

    public User(String username, String gender, String name, String phone, String dob, String email){
        this();
        this.username.set(username);
        this.gender.set(gender);
        this.name.set(name);
        this.phone.set(phone);
        this.dob.set(dob);
        this.email.set(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "username=" + username +
                ", gender=" + gender +
                ", name=" + name +
                ", phone=" + phone +
                ", dob=" + dob +
                ", email=" + email +
                '}';
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
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


}
