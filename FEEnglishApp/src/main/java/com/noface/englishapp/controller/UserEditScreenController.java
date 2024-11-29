package com.noface.englishapp.controller;

import com.noface.englishapp.model.User;
import com.noface.englishapp.utils.ResourceLoader;
import com.noface.englishapp.view.UserEditScreen;

import java.io.IOException;

public class UserEditScreenController {
    private UserEditScreen screen;
    private User user;
    public UserEditScreenController() throws IOException {
        user = ResourceLoader.getInstance().userCRUD.getUserInfo();
        screen = new UserEditScreen(this);
    }

    public UserEditScreen getScreen() {
        return screen;
    }

    public User getUser() {
        return user;
    }

    public int editUser(String newPassword) {
        int status = ResourceLoader.getInstance().userCRUD().editUser(user, newPassword);
        return status;
    }


}
