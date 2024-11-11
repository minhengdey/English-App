package org.example.englishapp_callapigemini;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.util.Objects;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Gemini API Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // Test
    /*On weekends, my family and I enjoy spending time together. Saturday mornings usually start with a big breakfast at home. My mom makes pancakes or waffles, and we all sit around the table chatting about our plans for the day. Sometimes we decide to go hiking in the nearby forest. It’s peaceful and beautiful there, with tall trees and birds chirping.*/

}
