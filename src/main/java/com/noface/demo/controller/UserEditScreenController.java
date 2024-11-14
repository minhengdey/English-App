package com.noface.demo.controller;

import com.noface.demo.model.User;
import com.noface.demo.screen.UserEditScreen;

import java.io.IOException;

public class UserEditScreenController {
    private UserEditScreen screen;
    private User user;
    public UserEditScreenController() throws IOException {
        user = new User();
        screen = new UserEditScreen(this);
    }

    public UserEditScreen getScreen() {
        return screen;
    }

    public User getUser() {
        return user;
    }

    public void saveUser() {

    }
}
