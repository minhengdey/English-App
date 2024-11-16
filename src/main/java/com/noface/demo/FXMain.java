package com.noface.demo;

import com.noface.demo.controller.LoginScreenController;
import com.noface.demo.controller.MainController;

import com.noface.demo.controller.TopicScreenController;
import com.noface.demo.controller.UserEditScreenController;
import com.noface.demo.screen.MainScreen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.HTMLEditorSkin;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMain extends Application{

    @Override
    public void init(){
        // context = new SpringApplicationBuilder(SpringMain.class).run();
    }
    public void start(Stage stage) throws Exception {
        stage.resizableProperty().set(false);
//        testMainScreen(stage);
        testLogin(stage);
//        testEditProfile(stage);
//        testListTopicScreen(stage);
//        testTranslateMainScreen(stage);
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
