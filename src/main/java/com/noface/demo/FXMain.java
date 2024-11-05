package com.noface.demo;

import org.springframework.context.ConfigurableApplicationContext;

import com.noface.demo.card.CardController;
import com.noface.demo.card.CardLearningUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMain extends Application{
    private ConfigurableApplicationContext context;

    @Override
    public void init(){
        // context = new SpringApplicationBuilder(SpringMain.class).run();
    }
    public void start(Stage stage) throws Exception {
        System.out.println("Hello world");
        FXMLLoader loader = new FXMLLoader(CardLearningUI.class.getResource("CardLearningUI.fxml"));
        // FXMLLoader loader = new FXMLLoader(BrowseUI.class.getResource("BrowseUI.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        CardController cardController = new CardController(loader);

    }
    public static void main(String[] args) {
        launch(args);
//        System.out.println(LocalDateTime.now());
    }
}
