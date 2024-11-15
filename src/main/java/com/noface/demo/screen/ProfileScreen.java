package com.noface.demo.screen;

import com.noface.demo.controller.UserEditScreenController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;

public class ProfileScreen {
    private FXMLLoader loader;
    private MainScreen mainScreen;
    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
    public ProfileScreen() throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("ProfileScreen.fxml"));
        loader.setController(this);
        loader.load();
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("edit button clicked");
                try {
                    handleEditButtonClicked(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void handleEditButtonClicked(ActionEvent event) throws IOException {
        showTypePassworDialog();

    }
    public void showEditUserScreen() throws IOException {
        Stage stage = new Stage();
        UserEditScreenController controller = new UserEditScreenController();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(controller.getScreen().getRoot()));
        stage.show();
    }
    public void showTypePassworDialog(){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login");
        dialog.setHeaderText("Enter your credentials");

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Enable/Disable login button depending on whether a password was entered
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        // Convert the result to a username-password pair when the login button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(credentials -> {
            System.out.println("Username=" + credentials.getKey() + ", Password=" + credentials.getValue());
            if(credentials.getKey().equals("1") && credentials.getValue().equals("1")){
                try {
                    showEditUserScreen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                showAlert("Tài khoản hoặc mật khẩu không hợp lệ, vui lòng thử lại", Alert.AlertType.WARNING);
                showTypePassworDialog();
            }
        });
    }
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public <T> T getRoot(){
        return loader.getRoot();
    }
    @FXML
    private Button editButton;


}
