package com.noface.demo.screen;

import javafx.fxml.FXMLLoader;

public abstract class Screen {
    protected FXMLLoader loader;
    public <T> T getRoot(){
        return loader.getRoot();
    }
}
