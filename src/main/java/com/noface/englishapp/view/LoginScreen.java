package com.noface.englishapp.view;

import com.noface.englishapp.controller.LoginScreenController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
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
    private Button forgetPasswordButton;
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

    public void refreshData() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public void configureScreenComponentEventHandler(LoginScreenController controller){
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                int loginStatus = controller.handleLogin();
                if(loginStatus == LoginScreenController.LOGIN_SUCCESSFUL){
                    showMainScreen(controller.getMainController().getMainScreen());
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
        forgetPasswordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                controller.handleForgetPassword();
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
    public void showMainScreen(MainScreen mainScreen) {
        try {

            Stage stage = (Stage) ((Node) this.getRoot()).getScene().getWindow();
            stage.setResizable(true);
            stage.setTitle("Homepage");
            mainScreen.refresh();
            mainScreen.changeToListTopicPane();
            mainScreen.setLoginScreen(this);
            mainScreen.changeToDictionaryScreen();
            Parent root = mainScreen.getRoot();
            Scene scene;
            if(root.getScene() == null){
                scene = new Scene(root);
            }else{
                scene = root.getScene();
            }
            stage.setScene(scene);



            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();


            stage.setX((screenWidth - stage.getWidth()) / 2);
            stage.setY((screenHeight - stage.getHeight()) / 2);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
