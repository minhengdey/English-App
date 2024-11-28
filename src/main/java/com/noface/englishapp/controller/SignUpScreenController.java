package com.noface.englishapp.controller;

import com.noface.englishapp.view.SignUpScreen;

import java.io.IOException;

public class SignUpScreenController {
    private SignUpScreen screen;
    public SignUpScreenController() throws IOException {
        screen = new SignUpScreen(this);
    }

    public SignUpScreen getScreen() {
        return screen;
    }
}
