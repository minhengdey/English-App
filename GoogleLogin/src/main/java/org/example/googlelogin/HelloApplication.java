package org.example.googlelogin;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private static final String GOOGLE_AUTH_URL = "http://localhost:8080/oauth2/authorization/google";

    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Tải trang đăng nhập Google
        webEngine.load(GOOGLE_AUTH_URL);

        // Xử lý sự kiện khi URL thay đổi
        webEngine.locationProperty().addListener((obs, oldUrl, newUrl) -> {
            if (newUrl.startsWith("http://localhost:8080/token=")) {
                String token = newUrl.substring("http://localhost:8080/token=".length());
                System.out.println(token);
                primaryStage.close();
            }
            if (newUrl.contains("login/oauth2/code/google")) {
                // Xử lý mã Authorization Code nếu cần thiết
                System.out.println("Redirected back to: " + newUrl);
            }
        });

        BorderPane root = new BorderPane();
        root.setCenter(webView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Google Login");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
