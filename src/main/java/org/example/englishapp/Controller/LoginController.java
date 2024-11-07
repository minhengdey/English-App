package org.example.englishapp.Controller;

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

    @FXML
    TextField fullNameField;
    @FXML
    TextField registerNameField;
    @FXML
    PasswordField registerPasswordField;
    @FXML
    PasswordField confirmPasswordField;

    private HttpClient httpClient = HttpClient.newHttpClient();

    private String normalize_name(String name)
    {
        String[] k = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String i : k)
        {
            sb.append(i.substring(0, 1).toUpperCase())
                    .append(i.substring(1).toLowerCase())
                    .append(" ");
        }
        return sb.toString().trim();
    }

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
                    password);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println (response.statusCode());
            if (response.statusCode() == 200) System.out.println ("OK");
            else System.out.println ("Failed");
        }
        catch (Exception e)
        {
            e.printStackTrace();
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


            signUpStage.setWidth(400);
            signUpStage.setHeight(600);


            signUpStage.setX((screenWidth - signUpStage.getWidth()) / 2);
            signUpStage.setY((screenHeight - signUpStage.getHeight()) / 2);

            signUpStage.setResizable(false);
            signUpStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void handleRegisterButton(javafx.event.ActionEvent event) {
        try
        {
            String fullName = normalize_name(fullNameField.getText());
            String username = registerNameField.getText();
            String password = registerPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (!password.equals(confirmPassword)) showAlert("Mật khẩu không khớp!", Alert.AlertType.ERROR);
            else
            {
                String requestBody = String.format (
                        "{" +
                                "\"username\":\"%s\"," +
                                "\"password\":\"%s\"," +
                                "\"name\":\"%s\"," +
                                "\"day\":22," +
                                "\"month\":12," +
                                "\"year\":2004," +
                                "\"gender\":\"Nam\"," +
                                "\"email\":\"nguyenduyanh221204@gmail.com\"," +
                                "\"phone\":\"0901752586\"" +
                        "}",
                        username,
                        password,
                        fullName
                );
                System.out.println (requestBody);
                try
                {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:8080/users"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    //System.out.println (response.statusCode());
                    //System.out.println (response.body());
                    if (response.statusCode() == 200)
                    {
                        showAlert("Đăng ký thành công!", Alert.AlertType.INFORMATION);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();
                    }
                    else showAlert("Đăng ký thất bại!", Alert.AlertType.ERROR);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    showAlert("Có lỗi xảy ra!", Alert.AlertType.ERROR);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();;
        }
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


