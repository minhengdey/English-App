package com.noface.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noface.demo.screen.LoginScreen;
import com.noface.demo.resource.TokenManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginScreenController {
    public static final int LOGIN_SUCCESSFUL = 1;
    public static final int LOGIN_ERROR = 2;
    private LoginScreen screen;
    private StringProperty username = new SimpleStringProperty();
    private StringProperty password =  new SimpleStringProperty();
    private MainController mainController;
    public LoginScreenController() throws IOException {
        screen = new LoginScreen(this);
        username.bind(screen.getUsernameField().textProperty());
        password.bind(screen.getPasswordField().textProperty());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public LoginScreen getScreen() {
        return screen;
    }

    private HttpClient httpClient = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    public int handleLogin() {
        return handleLogin(username.get(), password.get());
    }
    public int handleLogin(String username, String password){
        try
        {
            String requestBody = String.format (
                    "{" +
                            "\"username\":\"%s\"," +
                            "\"password\":\"%s\"" +
                            "}",
                    username,
                    password
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
                return LOGIN_SUCCESSFUL;
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
        return LOGIN_ERROR;
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


    private static final String GOOGLE_AUTH_URL = "http://localhost:8080/oauth2/authorization/google";

    public void handleGoogleLogin() {

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();


        webEngine.load(GOOGLE_AUTH_URL);


        webEngine.locationProperty().addListener((obs, oldUrl, newUrl) -> {
            if (newUrl.contains("login/oauth2/code/google")) {

                System.out.println("Redirected back to: " + newUrl);

            }
        });


        Stage stage = new Stage();
        BorderPane root = new BorderPane();
        root.setCenter(webView);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Google Login");
        stage.show();
    }

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

    public void setUsername(String username) {
        this.username.set(username);
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

}


