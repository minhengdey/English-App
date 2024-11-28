package com.noface.englishapp.view;

import com.noface.englishapp.controller.UserEditScreenController;
import com.noface.englishapp.model.User;
import com.noface.englishapp.utils.UserCRUD;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class UserEditScreen {
    private FXMLLoader loader;
    private User user;
    private UserEditScreenController controller;
    public UserEditScreen() throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("UserEditScreen.fxml"));
        loader.setController(this);
        loader.load();
        this.user = user;
    }
    @FXML
    private TextField confirmPasswordTF;

    @FXML
    private TextField dayTF;

    @FXML
    private TextField emailTF;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private TextField monthTF;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField passwordTF;

    @FXML
    private TextField phoneTF;

    @FXML
    private TextField yearTF;

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    @FXML
    void initialize() {
        genderChoiceBox.getItems().addAll("Male", "Female", "other");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleSaveButton(event);
            }
        });
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleCancelButtonClicked(event);
            }
        });
    }


    public UserEditScreen(UserEditScreenController controller) throws IOException {
        this();
        this.controller = controller;
        this.user = controller.getUser();

        nameTF.setText(user.getName());
        emailTF.setText(user.getEmail());
        phoneTF.setText(user.getPhone());
        LocalDate date = LocalDate.parse(user.getDob());
        dayTF.setText(String.valueOf(date.getDayOfMonth()));
        monthTF.setText(String.valueOf(date.getMonth().getValue()));
        yearTF.setText(String.valueOf(date.getYear()));
        genderChoiceBox.setValue(user.getGender());

        user.nameProperty().bind(nameTF.textProperty());
        user.phoneProperty().bind(phoneTF.textProperty());
        user.emailProperty().bind(emailTF.textProperty());
        user.genderProperty().bind(genderChoiceBox.valueProperty());
        user.dobProperty().bind(Bindings.createStringBinding(() -> {
            return LocalDate.of(
                    Integer.parseInt(yearTF.getText()),
                    Integer.parseInt(monthTF.getText()),
                    Integer.parseInt(dayTF.getText())
            ).toString();
        }, yearTF.textProperty(), dayTF.textProperty(), monthTF.textProperty()));
    }


    public <T> T getRoot() {
        return loader.getRoot();
    }

    public void handleCancelButtonClicked(ActionEvent event){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    protected void handleSaveButton(ActionEvent event) {
        try {
            String fullName = nameTF.getText();
            String pass = passwordTF.getText();
            String confirmPass = confirmPasswordTF.getText();
            String d = dayTF.getText();
            String m = monthTF.getText();
            String y = yearTF.getText();
            String userGender = genderChoiceBox.getValue();
            String mail = emailTF.getText();
            String phoneNumber = phoneTF.getText();

            if (fullName.isEmpty()) {
                showAlert("Please enter your full name!", Alert.AlertType.WARNING);
            } else if (pass.length() < 8) {
                showAlert("Password must be at least 8 characters long!", Alert.AlertType.WARNING);
                passwordTF.clear();
                confirmPasswordTF.clear();
            } else if (!pass.equals(confirmPass)) {
                showAlert("Passwords do not match!", Alert.AlertType.WARNING);
                passwordTF.clear();
                confirmPasswordTF.clear();
            } else if (d.isEmpty() || m.isEmpty() || y.isEmpty()) {
                showAlert("Please enter your complete date of birth!", Alert.AlertType.WARNING);
            } else if (userGender == null) {
                showAlert("Please select a gender!", Alert.AlertType.WARNING);
            } else if (mail.isEmpty()) {
                showAlert("Please enter your email!", Alert.AlertType.WARNING);
            } else if (phoneNumber.isEmpty()) {
                showAlert("Please enter your phone number!", Alert.AlertType.WARNING);
            } else {
                try {
                    LocalDate.of(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d));
                } catch (Exception e) {
                    showAlert("Please enter a valid date of birth!", Alert.AlertType.ERROR);
                    dayTF.clear();
                    monthTF.clear();
                    yearTF.clear();
                    return;
                }
            }

            int status = controller.editUser(passwordTF.getText());
            if (status == UserCRUD.USER_EDITED_SUCCESSFUL) {
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Invalid information, please try again!", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
