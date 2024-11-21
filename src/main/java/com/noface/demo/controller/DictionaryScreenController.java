package com.noface.demo.controller;

import com.noface.demo.screen.DictionaryScreen;

import java.io.IOException;

public class DictionaryScreenController {
    private DictionaryScreen screen;
    public DictionaryScreenController() throws IOException {
        screen = new DictionaryScreen();
    }
    public DictionaryScreen getScreen(){
        return screen;
    }
}
