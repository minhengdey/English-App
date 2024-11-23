package org.example.facebooklogin;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    // URL đăng nhập Facebook (cấu hình client_id và redirect_uri chính xác)
    private static final String FACEBOOK_AUTH_URL =
            "https://www.facebook.com/v12.0/dialog/oauth" +
                    "?client_id=918699420184358" +
                    "&redirect_uri=http://localhost:8080/login/oauth2/code/facebook" +
                    "&response_type=code" +
                    "&scope=email,public_profile";

    // API để lấy thông tin từ backend (Spring Boot)
    private static final String BACKEND_API_URL = "http://localhost:8080/api/user-info";

    @Override
    public void start(Stage primaryStage) {
        // Nút để bắt đầu đăng nhập bằng Facebook
        Button loginButton = new Button("Đăng nhập bằng Facebook");
        loginButton.setOnAction(event -> {
            openFacebookLogin();
        });

        VBox root = new VBox(10, loginButton);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(root, 400, 200);
        primaryStage.setTitle("Facebook Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Mở trình duyệt ngoài để đăng nhập
    private void openFacebookLogin() {
        try {
            Desktop.getDesktop().browse(new URI(FACEBOOK_AUTH_URL));
            showAlert("Mở trình duyệt", "Vui lòng đăng nhập bằng Facebook trong trình duyệt.");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở trình duyệt.");
        }
    }

    // Gửi yêu cầu đến backend để lấy thông tin người dùng
    private void fetchUserInfo() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BACKEND_API_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Hiển thị thông tin người dùng nhận được từ backend
            showAlert("Thông tin người dùng", response.body());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể lấy thông tin người dùng.");
        }
    }

    // Hiển thị thông báo
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}