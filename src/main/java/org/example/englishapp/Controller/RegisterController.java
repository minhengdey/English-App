package org.example.englishapp.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    TextField name;
    @FXML
    TextField username;
    @FXML
    TextField password;
    @FXML
    TextField confirmPassword;
    @FXML
    TextField day;
    @FXML
    TextField month;
    @FXML
    TextField year;
    @FXML
    ChoiceBox<String> gender = new ChoiceBox<>();;
    @FXML
    TextField email;
    @FXML
    TextField phone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gender.getItems().addAll("Nam", "Nữ", "Khác");
    }
}
