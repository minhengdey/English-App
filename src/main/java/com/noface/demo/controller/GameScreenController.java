package com.noface.demo.controller;

import com.noface.demo.screen.GameScreen;

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
