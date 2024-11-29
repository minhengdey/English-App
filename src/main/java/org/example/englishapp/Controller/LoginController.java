package org.example.englishapp.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.example.englishapp.HelloApplication;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;

    private HttpClient httpClient = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

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
                handleLoginInterface();
            }
            else
            {
                String jsonResponse = response.body();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                int code = jsonNode.get("code").asInt();
                if (code == 1007) showAlert("Thông tin đăng nhập không hợp lệ, vui lòng kiểm tra lại!", Alert.AlertType.ERROR);
                else showAlert("Có lỗi xảy ra, vui lòng thử lại sau!", Alert.AlertType.ERROR);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            showAlert("Có lỗi xảy ra, vui lòng thử lại sau!", Alert.AlertType.ERROR);
        }
    }
    @FXML
    protected void handleCreateAccount() {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("sign_up.fxml")));

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

    @FXML
    protected void handleLoginInterface() {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("interface.fxml")));

            Stage signUpStage = new Stage();
            signUpStage.setTitle("Trang chủ");


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

    @FXML
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}


