package com.noface.demo;

import com.noface.demo.controller.*;

import com.noface.demo.model.UserCRUD;
import com.noface.demo.screen.DictionaryScreen;
import com.noface.demo.screen.MainScreen;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class FXMain extends Application{

    @Override
    public void init(){
        // context = new SpringApplicationBuilder(SpringMain.class).run();
    }
    public void start(Stage stage) throws Exception {
//        stage.resizableProperty().set(false);
        testLogin(stage);
//        testGame(stage);
    }
    public void testRandom(Stage stage) throws IOException {
        WordCombineGameController controller = new WordCombineGameController();
        Parent root = controller.getScreen().getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    private void testTranslateMainScreen(Stage stage) throws IOException {
        MainController controller = new MainController();
        MainScreen screen = controller.getMainScreen();
        screen.changeToTranslatePane();
        stage.setScene(new Scene(screen.getRoot()));
        stage.show();
    }

    private void testListTopicScreen(Stage stage) throws IOException {
        MainController controller = new MainController();
        MainScreen screen = controller.getMainScreen();
        TopicScreenController topicController = controller.getTopicScreenController();
        screen.changeToListTopicPane();
        stage.setScene(new Scene(screen.getRoot()));
        stage.show();
    }

    private void testEditProfile(Stage stage) throws IOException {
        UserEditScreenController controller = new UserEditScreenController();
        stage.setScene(new Scene(controller.getScreen().getRoot()));
        stage.show();
    }

    public void testLogin(Stage stage) throws IOException {
        LoginScreenController loginScreenController = new LoginScreenController();
        Scene scene = new Scene(loginScreenController.getScreen().getRoot());
        stage.setScene(scene);
        stage.show();
        loginScreenController.setMainController(new MainController());
    }

    public void testMainScreen(Stage stage) throws IOException {
        MainController mainController = new MainController();

        stage.setScene(new Scene(mainController.getMainScreen().getRoot()));
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
