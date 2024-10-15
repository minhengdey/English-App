package com.example.englishapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VocabularyApplication extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("English App");

        VocabularyController vocabularyController = new VocabularyController();
        VBox layout = vocabularyController.getRoot();

        primaryStage.setScene(new Scene(layout, 600, 400));
        primaryStage.show();
    }
}