package com.noface.englishapp;

import com.noface.englishapp.controller.*;

import com.noface.englishapp.view.MainScreen;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMain extends Application{

    public void start(Stage stage) throws Exception {
        LoginScreenController loginScreenController = new LoginScreenController();
        Scene scene = new Scene(loginScreenController.getScreen().getRoot());
        stage.setScene(scene);
        stage.show();
        loginScreenController.setMainController(new MainController());
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
