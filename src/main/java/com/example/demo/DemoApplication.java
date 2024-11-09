package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import com.example.demo.view.DictionaryLookupView;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class DemoApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        this.applicationContext = SpringApplication.run(DemoApplication.class);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(DictionaryLookupView.class.getResource("DictionaryLookupView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        primaryStage.setTitle("Dictionary Lookup");
        primaryStage.setScene(scene);
        //DictionaryLookupView dictionaryLookupView = applicationContext.getBean(DictionaryLookupView.class);
        primaryStage.show();
    }
}
