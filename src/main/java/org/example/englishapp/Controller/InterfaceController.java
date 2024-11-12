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

public class InterfaceController implements Initializable {
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;

    private HttpClient httpClient = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();



    @FXML
    protected void handleLogoutInterface(javafx.event.ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("login.fxml")));

            Stage logOutStage = new Stage();
            logOutStage.setTitle("English App");


            Scene scene = new Scene(root);
            logOutStage.setScene(scene);


            logOutStage.initModality(Modality.APPLICATION_MODAL);


            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();


            logOutStage.setWidth(1000);
            logOutStage.setHeight(637);


            logOutStage.setX((screenWidth - logOutStage.getWidth()) / 2);
            logOutStage.setY((screenHeight - logOutStage.getHeight()) / 2);

            logOutStage.setResizable(false);
            logOutStage.show();
        } catch (Exception e) {
            e.printStackTrace();
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


