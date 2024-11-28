package com.noface.englishapp.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noface.englishapp.controller.SignUpScreenController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SignUpScreen implements Initializable {
    private FXMLLoader loader;

    public SignUpScreen(SignUpScreenController controller) throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("SignUpScreen.fxml"));
        loader.setController(this);
        loader.load();
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleRegisterButton(event);
            }
        });
    }

    public <T> T getRoot() {
        return loader.getRoot();
    }

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
    ChoiceBox<String> gender = new ChoiceBox<>();
    @FXML
    Button registerButton;
    @FXML
    TextField email;
    @FXML
    TextField phone;

    private HttpClient httpClient = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    private String normalize_name(String name) {
        String[] k = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String i : k) {
            sb.append(i.substring(0, 1).toUpperCase())
                    .append(i.substring(1).toLowerCase())
                    .append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gender.getItems().addAll("Male", "Female", "Other");
    }

    @FXML
    protected void handleRegisterButton(javafx.event.ActionEvent event) {
        try {
            String fullName = name.getText();
            String user = username.getText().trim();
            String pass = password.getText();
            String confirmPass = confirmPassword.getText();
            String d = day.getText();
            String m = month.getText();
            String y = year.getText();
            String userGender = gender.getValue();
            String mail = email.getText();
            String phoneNumber = phone.getText();

            if (fullName.isEmpty()) showAlert("Please enter your full name!", Alert.AlertType.WARNING);
            else if (user.isEmpty()) showAlert("Please enter a username!", Alert.AlertType.WARNING);
            else if (user.length() < 3) {
                showAlert("Username must be at least 3 characters long!", Alert.AlertType.WARNING);
                username.clear();
            } else if (pass.length() < 8) {
                showAlert("Password must be at least 8 characters long!", Alert.AlertType.WARNING);
                password.clear();
                confirmPassword.clear();
            } else if (!pass.equals(confirmPass)) {
                showAlert("Passwords do not match!", Alert.AlertType.WARNING);
                password.clear();
                confirmPassword.clear();
            } else if (d.isEmpty() || m.isEmpty() || y.isEmpty())
                showAlert("Please enter a valid date of birth!", Alert.AlertType.WARNING);
            else if (userGender == null) showAlert("Please select your gender!", Alert.AlertType.WARNING);
            else if (mail.isEmpty()) showAlert("Please enter your email address!", Alert.AlertType.WARNING);
            else if (phoneNumber.isEmpty()) showAlert("Please enter your phone number!", Alert.AlertType.WARNING);
            else {
                try {
                    LocalDate.of(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d));
                } catch (Exception e) {
                    showAlert("Please enter a valid date of birth!", Alert.AlertType.ERROR);
                    day.clear();
                    month.clear();
                    year.clear();
                    return;
                }

                String requestBody = String.format(
                        "{" +
                                "\"username\":\"%s\"," +
                                "\"password\":\"%s\"," +
                                "\"name\":\"%s\"," +
                                "\"day\":%d," +
                                "\"month\":%d," +
                                "\"year\":%d," +
                                "\"gender\":\"%s\"," +
                                "\"email\":\"%s\"," +
                                "\"phone\":\"%s\"" +
                                "}",
                        user,
                        pass,
                        normalize_name(fullName),
                        Integer.parseInt(d),
                        Integer.parseInt(m),
                        Integer.parseInt(y),
                        userGender,
                        mail,
                        phoneNumber
                );

                System.out.println(requestBody);
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:8080/users"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println(response.statusCode());
                    System.out.println(response.body());

                    if (response.statusCode() == 200) {
                        showAlert("Registration successful!", Alert.AlertType.INFORMATION);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();
                    } else {
                        String jsonResponse = response.body();
                        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                        int code = jsonNode.get("code").asInt();
                        if (code == 1000) {
                            showAlert("Username already exists, please choose another username!", Alert.AlertType.ERROR);
                            username.clear();
                        } else if (code == 1006) {
                            showAlert("Invalid email!", Alert.AlertType.ERROR);
                            email.clear();
                        } else showAlert("An error occurred!", Alert.AlertType.ERROR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("An error occurred!", Alert.AlertType.ERROR);
                }
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

