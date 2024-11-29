package com.noface.englishapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noface.englishapp.utils.ResourceLoader;
import com.noface.englishapp.view.LoginScreen;
import com.noface.englishapp.utils.TokenManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
import java.util.Optional;

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
            if (newUrl.startsWith("http://localhost:8080/token=")) {
                System.out.println("Redirected back to: " + newUrl);
                String token = newUrl.substring("http://localhost:8080/token=".length());
                System.out.println(token);
                TokenManager.getInstance().setToken(token);
                Stage stage = (Stage) webView.getScene().getWindow();
                stage.close();
                screen.showMainScreen(mainController.getMainScreen());

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

    public void handleForgetPassword()
    {
        try
        {
            TextInputDialog emailDialog = new TextInputDialog();
            emailDialog.setTitle("Forgot password");
            emailDialog.setHeaderText("Enter your email");
            emailDialog.setContentText("Email:");

            Optional<String> emailResult = emailDialog.showAndWait();
            if (emailResult.isEmpty()) return;

            String email = emailResult.get().trim();
            String OTP = ResourceLoader.getInstance().userCRUD.getOTP(email);
            if (OTP == null || OTP.startsWith("Failed"))
            {
                showAlert("Failed to send OTP. Please try again!", Alert.AlertType.ERROR);
                return;
            }
            System.out.println (OTP);

            TextInputDialog otpDialog = new TextInputDialog();
            otpDialog.setTitle("OTP Verification");
            otpDialog.setHeaderText("Enter the OTP sent to your email");
            otpDialog.setContentText("OTP:");

            Optional<String> otpResult = otpDialog.showAndWait();
            if (otpResult.isEmpty()) return;

            String otp = otpResult.get();
            if (!otp.trim().equals(OTP.trim()))
            {
                showAlert("Incorrect OTP!", Alert.AlertType.ERROR);
                return;
            }

            Dialog<String> passwordDialog = new Dialog<>();
            passwordDialog.setTitle("Change password");
            passwordDialog.setHeaderText("Enter your new password");

            PasswordField newPasswordField = new PasswordField();
            newPasswordField.setPromptText("New password");

            PasswordField confirmPasswordField = new PasswordField();
            confirmPasswordField.setPromptText("Confirm new password");

            VBox passwordBox = new VBox(10, newPasswordField, confirmPasswordField);
            passwordBox.setPadding(new Insets(10));
            passwordDialog.getDialogPane().setContent(passwordBox);

            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            passwordDialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            passwordDialog.setResultConverter(dialogButton ->
            {
                if (dialogButton == updateButtonType)
                {
                    if (!newPasswordField.getText().equals(confirmPasswordField.getText()))
                    {
                        showAlert("Confirmation password does not match!", Alert.AlertType.ERROR);
                        return null;
                    }
                    if (newPasswordField.getText().length() < 8)
                    {
                        showAlert("Password must contain at least 8 characters!", Alert.AlertType.ERROR);
                        return null;
                    }
                    return newPasswordField.getText();
                }
                return null;
            });

            Optional<String> passwordResult = passwordDialog.showAndWait();
            if (passwordResult.isEmpty()) return;

            String newPassword = passwordResult.get();
            int status = ResourceLoader.getInstance().userCRUD.forgotPassword(email, newPassword);

            if (status == 1) showAlert("Password updated successfully!", Alert.AlertType.INFORMATION);
            else showAlert("An error occurred. Please try again!", Alert.AlertType.ERROR);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            showAlert("An error occurred. Please try again!", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType alertType)
    {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

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
