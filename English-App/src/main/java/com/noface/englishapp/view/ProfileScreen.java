package com.noface.englishapp.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noface.englishapp.controller.ProfileScreenController;
import com.noface.englishapp.controller.UserEditScreenController;
import com.noface.englishapp.utils.TokenManager;
import com.noface.englishapp.utils.Utilities;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class ProfileScreen {
    private FXMLLoader loader;
    private MainScreen mainScreen;
    private ProfileScreenController profileScreenController;
    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
    public ProfileScreen(ProfileScreenController profileScreenController) throws IOException {
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
        this.profileScreenController = profileScreenController;
        bindToControllerProperty();
    }
    @FXML
    public void initialize(){

    }
    @FXML
    private TextField name, phone, gender, email, day, month, year;
    private TextField  username = new TextField();

    private void bindToControllerProperty() {
        name.textProperty().bind(profileScreenController.nameProperty());
        phone.textProperty().bind(profileScreenController.phoneProperty());
        gender.textProperty().bind(profileScreenController.genderProperty());
        email.textProperty().bind(profileScreenController.emailProperty());
        username.textProperty().bind(profileScreenController.usernameProperty());
        day.textProperty().bind(profileScreenController.dayProperty());
        month.textProperty().bind(profileScreenController.monthProperty());
        year.textProperty().bind(profileScreenController.yearProperty());
    }


    private void handleEditButtonClicked(ActionEvent event) throws IOException {
        showTypePassworDialog();
    }
    public void showEditUserScreen() throws IOException {
        Stage stage = new Stage();
        UserEditScreenController controller = new UserEditScreenController();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(controller.getScreen().getRoot()));
        stage.showAndWait();
        profileScreenController.refreshUserInfo();
    }
    public void showTypePassworDialog(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Login");
        dialog.setHeaderText("Enter your credentials");

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Password:"), 0, 0);
        grid.add(password, 1, 0);

        dialog.getDialogPane().setContent(grid);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return String.valueOf(dialogButton);
            }
            return null;
        });
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> {
            try {
                String requestBody = String.format (
                        "{" +
                                "\"username\":\"%s\"," +
                                "\"password\":\"%s\"" +
                                "}",
                        username.getText(),
                        password.getText()
                );

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://localhost:8080/auth/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200)
                {
                    String jsonResponse = response.body();
                    JsonNode jsonNode = new ObjectMapper().readTree(jsonResponse);
                    String token = jsonNode.path("result").path("token").asText();
                    System.out.println (token);
                    TokenManager.getInstance().setToken(token);
                    showEditUserScreen();
                }else{
                    Utilities.getInstance().showAlert("Không hợp lệ, vui lòng nhập lại", Alert.AlertType.WARNING);
                    showTypePassworDialog();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
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
