package com.noface.demo.screen;

import com.noface.demo.controller.UserEditScreenController;
import com.noface.demo.model.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UserEditScreen {
    private FXMLLoader loader;
    private User user;
    private UserEditScreenController controller;
    public UserEditScreen() throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("UserEditScreen.fxml"));
        loader.setController(this);
        loader.load();
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
        genderChoiceBox.getItems().addAll("Nam", "Nữ", "Khác");
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
    }


    public <T> T getRoot() {
        return loader.getRoot();
    }

    public void handleCancelButtonClicked(ActionEvent event){
        Stage stage = (Stage) ((Node) (loader.getRoot())).getScene().getWindow();
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

            if (fullName.isEmpty()) showAlert("Vui lòng điền họ tên đầy đủ!", Alert.AlertType.WARNING);
            else if (pass.length() < 8) {
                showAlert("Mật khẩu phải chứa ít nhất 8 ký tự!", Alert.AlertType.WARNING);
                passwordTF.clear();
                confirmPasswordTF.clear();
            } else if (!pass.equals(confirmPass)) {
                showAlert("Mật khẩu không khớp!", Alert.AlertType.WARNING);
                passwordTF.clear();
                confirmPasswordTF.clear();
            } else if (d.isEmpty() || m.isEmpty() || y.isEmpty())
                showAlert("Vui lòng điền đầy đủ ngày sinh!", Alert.AlertType.WARNING);
            else if (userGender == null) showAlert("Vui lòng chọn giới tính!", Alert.AlertType.WARNING);
            else if (mail.isEmpty()) showAlert("Vui lòng điền email!", Alert.AlertType.WARNING);
            else if (phoneNumber.isEmpty()) showAlert("Vui lòng điền số điện thoại!", Alert.AlertType.WARNING);
            else {
                try {
                    LocalDate.of(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d));
                } catch (Exception e) {
                    showAlert("Vui lòng điền ngày sinh hợp lệ!", Alert.AlertType.ERROR);
                    dayTF.clear();
                    monthTF.clear();
                    yearTF.clear();
                    return;
                }
            }
            controller.saveUser();
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
