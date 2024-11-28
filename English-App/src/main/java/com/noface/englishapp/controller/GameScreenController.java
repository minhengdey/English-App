package com.noface.englishapp.controller;

import com.noface.englishapp.view.GameScreen;

import java.io.IOException;

public class GameScreenController {
    private GameScreen screen;
    public GameScreenController() throws IOException {
        screen = new GameScreen(this);
    }

    public GameScreen getScreen() {
        return screen;
    }
}
