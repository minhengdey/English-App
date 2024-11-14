package com.noface.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noface.demo.HelloApplication;
import com.noface.demo.screen.LoginScreen;
import com.noface.demo.utils.TokenManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class LoginScreenController {
    private LoginScreen screen;
    private StringProperty username = new SimpleStringProperty();
    private StringProperty password =  new SimpleStringProperty();
    public LoginScreenController() throws IOException {
        screen = new LoginScreen(this);
        username.bind(screen.getUsernameField().textProperty());
        password.bind(screen.getPasswordField().textProperty());
    }

    public LoginScreen getScreen() {
        return screen;
    }

    private HttpClient httpClient = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void handleLogin() {


        try
        {
            String requestBody = String.format (
                    "{" +
                            "\"username\":\"%s\"," +
                            "\"password\":\"%s\"" +
                    "}",
                    username.get(),
                    password.get()
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println (response.statusCode());

            if (response.statusCode() == 200)
            {
                String jsonResponse = response.body();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                String token = jsonNode.path("result").path("token").asText();
                System.out.println (token);
                TokenManager.getInstance().setToken(token);
                handleLoginInterface();
            }
            else
            {
                String jsonResponse = response.body();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                int code = jsonNode.get("code").asInt();
                if (code == 1002 || code == 1007) showAlert("Thông tin đăng nhập không hợp lệ, vui lòng kiểm tra lại!", Alert.AlertType.ERROR);
                else showAlert("Có lỗi xảy ra, vui lòng thử lại sau!", Alert.AlertType.ERROR);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            showAlert("Có lỗi xảy ra, vui lòng thử lại sau!", Alert.AlertType.ERROR);
        }
    }
    public void handleSignUp() {
        try {
            SignUpScreenController controller = new SignUpScreenController();
            Parent root = controller.getScreen().getRoot();

            Stage signUpStage = new Stage();
            signUpStage.setTitle("Đăng ký");


            Scene scene = new Scene(root);
            signUpStage.setScene(scene);


            signUpStage.initModality(Modality.APPLICATION_MODAL);


            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();


            signUpStage.setWidth(1000);
            signUpStage.setHeight(637);


            signUpStage.setX((screenWidth - signUpStage.getWidth()) / 2);
            signUpStage.setY((screenHeight - signUpStage.getHeight()) / 2);

            signUpStage.setResizable(false);
            signUpStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void handleLoginInterface() {
        try {

            MainController controller = new MainController();

            Stage stage = (Stage) ((Node) screen.getRoot()).getScene().getWindow();
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
//
    private void showAlert(String message, Alert.AlertType alertType)
    {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//    }
}


