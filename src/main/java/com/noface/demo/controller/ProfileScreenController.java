package com.noface.demo.controller;

import com.noface.demo.screen.ProfileScreen;

import java.io.IOException;

public class ProfileScreenController {
    private ProfileScreen screen;
    public ProfileScreenController() throws IOException {
        screen = new ProfileScreen();
    }

    public ProfileScreen getScreen() {
        return screen;
    }



}
