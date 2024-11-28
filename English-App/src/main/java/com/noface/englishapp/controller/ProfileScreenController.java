package com.noface.englishapp.controller;

import com.noface.englishapp.model.User;
import com.noface.englishapp.utils.ResourceLoader;
import com.noface.englishapp.view.ProfileScreen;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.time.LocalDate;

public class ProfileScreenController {
    private StringProperty username  = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty phone = new SimpleStringProperty();
    private StringProperty address = new SimpleStringProperty();
    private StringProperty gender = new SimpleStringProperty();
    private StringProperty day = new SimpleStringProperty();
    private StringProperty month = new SimpleStringProperty();
    private StringProperty year = new SimpleStringProperty();
    private ProfileScreen screen;
    private User user;
    public ProfileScreenController() throws IOException {
        screen = new ProfileScreen(this);
    }

    public ProfileScreen getScreen() {
        return screen;
    }

    public void refreshUserInfo(){
        user = ResourceLoader.getInstance().userCRUD.getUserInfo();
        this.name.set(user.getName());
        this.email.set(user.getEmail());
        this.phone.set(user.getPhone());
        LocalDate date = LocalDate.parse(user.getDob());
        this.day.set(String.valueOf(date.getDayOfMonth()));
        this.month.set(String.valueOf(date.getMonthValue()));
        this.year.set(String.valueOf(date.getYear()));
        this.username.set(user.getUsername());
        this.gender.set(user.getGender());
        this.username.set(user.getUsername());
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
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

    public String getDay() {
        return day.get();
    }

    public StringProperty dayProperty() {
        return day;
    }

    public String getMonth() {
        return month.get();
    }

    public StringProperty monthProperty() {
        return month;
    }

    public String getYear() {
        return year.get();
    }

    public StringProperty yearProperty() {
        return year;
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }
}
