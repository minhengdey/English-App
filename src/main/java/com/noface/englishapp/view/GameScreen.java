package com.noface.englishapp.view;

import com.noface.englishapp.controller.GameScreenController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class GameScreen {
    private FXMLLoader loader;
    private MainScreen mainScreen;
    public GameScreen(GameScreenController gameScreenController) throws IOException {
        loader = new FXMLLoader(getClass().getResource("GameScreen.fxml"));
        loader.setController(this);
        loader.load();
    }

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

    public <T> T getRoot() {
        return loader.getRoot();
    }

    @FXML
    private HBox wordGameButton;

    @FXML
    private HBox listenGameButton;

    @FXML
    public void initialize(){
        wordGameButton.setOnMouseClicked(wordGameButtonMouseClicked());
        listenGameButton.setOnMouseClicked(listenGameButtonMouseClicked());
    }

    private EventHandler<? super MouseEvent> listenGameButtonMouseClicked() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mainScreen.changeToListenGamePane();
            }
        };
    }

    private EventHandler<? super MouseEvent> wordGameButtonMouseClicked() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mainScreen.changeToWordGamePane();
            }
        };
    }

}
