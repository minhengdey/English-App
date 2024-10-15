package com.noface.demo;

import com.noface.demo.controller.CardController;
import com.noface.demo.view.BrowseUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Hello world");
        FXMLLoader loader = new FXMLLoader(BrowseUI.class.getResource("BrowseUI.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        BrowseUI browseUI = loader.getController();
        CardController cardController = new CardController(browseUI);
        cardController.updateViewCard();
    }

    public static void main(String[] args) {
        launch(Main.class,args);

    }
}
