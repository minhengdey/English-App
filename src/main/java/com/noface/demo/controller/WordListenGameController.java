package com.noface.demo.controller;

import com.noface.demo.screen.WordListenGameScreen;

import java.io.IOException;

public class WordListenGameController {
    private WordListenGameScreen screen;
    public WordListenGameController() throws IOException {
        screen = new WordListenGameScreen(this);
    }

    public WordListenGameScreen getScreen() {
        return screen;
    }
}
