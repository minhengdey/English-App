package com.noface.demo.screen;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class TranslateScreen {
    private FXMLLoader loader;

    public TranslateScreen() throws IOException {
       loader = new FXMLLoader(this.getClass().getResource("TranslateScreen.fxml"));
       loader.setController(this);
       loader.load();
    }
    public <T> T getRoot(){
        return loader.getRoot();
    }
}
