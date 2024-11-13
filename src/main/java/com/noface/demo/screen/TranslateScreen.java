package com.noface.demo.screen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TranslateScreen implements Initializable {
    private FXMLLoader loader;
    private MainScreen mainScreen;

    public TranslateScreen() throws IOException {
       loader = new FXMLLoader(this.getClass().getResource("TranslateScreen.fxml"));
       loader.setController(this);
       loader.load();
    }
    public <T> T getRoot(){
        return loader.getRoot();
    }

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

    private static final String API_KEY = "AIzaSyBeampUMDA65ov5E_UO5XqrR1vZFlssaJI";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";
    private ExecutorService executorService;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private Button translateButton;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private ComboBox<String> comboBox;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Here is translate function");
        comboBox.getItems().addAll("English To Vietnamese", "Vietnamese To English");
        executorService = Executors.newSingleThreadExecutor();
        translateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                generateContent(event);
            }
        });
    }

    private String sendApiRequest(String prompt) throws IOException {
        URL url = new URL(API_URL + "?key=" + API_KEY);
        HttpURLConnection connection = getHttpURLConnection(prompt, url);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    private static HttpURLConnection getHttpURLConnection(String prompt, URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }] }";
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }
    private String parseResponse(String response) throws IOException {
        int startIndex = response.indexOf("\"text\": \"") + 9;
        int endIndex = response.indexOf("\"", startIndex);
        return response.substring(startIndex, endIndex - 3);

    }

    private void generateContent(ActionEvent actionEvent) {
        String prompt = inputTextArea.getText();
        if (comboBox.getValue().equals("English To Vietnamese")) {
            prompt = "Dịch sang Tiếng Việt " + prompt;
        } else {
            prompt = "Dịch sang Tiếng Anh " + prompt;
        }
        String finalPrompt = prompt;
        System.out.println(finalPrompt);
        executorService.submit(() -> {
            try {
                String response = sendApiRequest(finalPrompt);

                String generatedText = parseResponse(response);
                javafx.application.Platform.runLater(() -> outputTextArea.setText(generatedText));
                System.out.println(generatedText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

}
