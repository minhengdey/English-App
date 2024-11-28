package com.noface.englishapp.controller;

import com.noface.englishapp.view.TranslateScreen;

import java.io.IOException;

public class TranslateScreenController {
    private TranslateScreen screen;
    public TranslateScreenController() throws IOException {
        screen = new TranslateScreen();
    }

    public TranslateScreen getScreen() {
        return screen;
    }
}
