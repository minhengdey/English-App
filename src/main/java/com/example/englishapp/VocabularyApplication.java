package com.example.englishapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class VocabularyApplication extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("library.fxml")));
            Scene scene = new Scene(root, 1000, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("English App");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}