package com.noface.demo;

import javax.swing.Spring;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noface.demo.controller.CardController;
import com.noface.demo.view.BrowseUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMain extends Application{
    private ConfigurableApplicationContext context;

    @Override
    public void init(){
        context = new SpringApplicationBuilder(SpringMain.class).run();
    }
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
        launch(args);

    }
}
