package com.noface.englishapp.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserRequest {
    private StringProperty username, name, gender, email, phone, password;
    private IntegerProperty day, month, year;
    public UserRequest(){
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        name = new SimpleStringProperty();
        day = new SimpleIntegerProperty();
        month = new SimpleIntegerProperty();
        year = new SimpleIntegerProperty();
        gender = new SimpleStringProperty();
        email = new SimpleStringProperty();
        phone = new SimpleStringProperty();
    }


    public UserRequest(String username, String password, String name, int day, int month, int year, String gender, String email, String phone) {
        this();
        this.username.set(username);
        this.name.set(name);
        this.day.set(day);
        this.month.set(month);
        this.year.set(year);
        this.gender.set(gender);
        this.email.set(email);
        this.phone.set(phone);
        this.password.set(password);
    }

    public UserRequest(String username,  String name, int day, int month, int year, String gender, String email, String phone) {
        this();
        this.username.set(username);
        this.name.set(name);
        this.day.set(day);
        this.month.set(month);
        this.year.set(year);
        this.gender.set(gender);
        this.email.set(email);
        this.phone.set(phone);
    }




    @Override
    public String toString() {
        return "UserRequest{" + "username=" + username + ", name=" + name + ", day=" + day + ", month=" + month + ", year=" + year + ", gender=" + gender + ", email=" + email + ", phone=" + phone + '}';
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

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getDay() {
        return day.get();
    }

    public IntegerProperty dayProperty() {
        return day;
    }

    public int getMonth() {
        return month.get();
    }

    public IntegerProperty monthProperty() {
        return month;
    }

    public int getYear() {
        return year.get();
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }
}
