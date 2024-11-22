package com.noface.demo.screen;

import com.noface.demo.controller.LoginScreenController;
import com.noface.demo.controller.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

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

                int loginStatus = controller.handleLogin();
                if(loginStatus == LoginScreenController.LOGIN_SUCCESSFUL){
                    showMainScreen();
                }
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
    protected void showMainScreen() {
        try {

            MainController controller = new MainController();

            Stage stage = (Stage) ((Node) this.getRoot()).getScene().getWindow();
            stage.setTitle("Trang chủ");


            Scene scene = new Scene(controller.getMainScreen().getRoot());
            stage.setScene(scene);



            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();


            stage.setWidth(1000);
            stage.setHeight(637);


            stage.setX((screenWidth - stage.getWidth()) / 2);
            stage.setY((screenHeight - stage.getHeight()) / 2);

            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
