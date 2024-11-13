package com.noface.demo.screen;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ProfileScreen {
    private FXMLLoader loader;
    private MainScreen mainScreen;
    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
    public ProfileScreen() throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("ProfileScreen.fxml"));
        loader.setController(this);
        loader.load();
    }
    public <T> T getRoot(){
        return loader.getRoot();
    }

}
