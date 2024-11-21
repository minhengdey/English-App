package com.noface.demo.screen;

import com.noface.demo.controller.LoginScreenController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginScreen {
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Button googleLoginButton;

    private FXMLLoader loader;
    public LoginScreen(LoginScreenController controller) throws IOException {
        this.loader = new FXMLLoader(this.getClass().getResource("LoginScreen.fxml"));
        loader.setController(this);
        loader.load();
        configureScreenComponentEventHandler(controller);
    }
    public void configureScreenComponentEventHandler(LoginScreenController controller){
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleLogin();
            }
        });
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleSignUp();
            }
        });
        googleLoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleGoogleLogin();
            }
        });
    }



    public PasswordField getPasswordField() {
        return passwordField;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public <T> T getRoot(){
        return loader.getRoot();
    }
}
