package com.noface.demo.controller;

import com.noface.demo.model.User;
import com.noface.demo.resource.ResourceLoader;
import com.noface.demo.screen.ProfileScreen;

import java.io.IOException;

public class ProfileScreenController {
    private ProfileScreen screen;
    private User user;
    public ProfileScreenController() throws IOException {
        screen = new ProfileScreen(this);
    }

    public ProfileScreen getScreen() {
        return screen;
    }

    public User getUser() {
        user = ResourceLoader.getInstance().userCRUD.getUserInfo();
        return user;
    }
}
