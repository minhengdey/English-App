package com.noface.demo.controller;

import com.noface.demo.screen.TranslateScreen;

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
