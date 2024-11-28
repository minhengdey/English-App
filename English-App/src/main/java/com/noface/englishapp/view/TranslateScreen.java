package com.noface.englishapp.view;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
        comboBox.getItems().addAll("English To Vietnamese", "Vietnamese To English");
        comboBox.getSelectionModel().select(0);
        executorService = Executors.newSingleThreadExecutor();
        translateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                generateContent(event);
            }
        });
    }
    public static List<String> splitTextBySentences(String text) {
        List<String> sentences = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '.') {
                sentences.add(text.substring(start, i + 1).trim());
                start = i + 1;
            }
        }
        if (start < text.length()) {
            sentences.add(text.substring(start).trim());
        }
        return sentences;
    }

    private String sendApiRequest(String text, String sourceLang, String targetLang) throws Exception {
        String urlStr = String.format(
                "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s",
                sourceLang, targetLang, java.net.URLEncoder.encode(text, StandardCharsets.UTF_8)
        );

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
            return parseResponse(response.toString());
        }
    }

    private String parseResponse(String response) {
        response = response.substring(4, response.indexOf("\",\""));
        return response;
    }

    @FXML
    private void generateContent(ActionEvent actionEvent) {
        String prompt = inputTextArea.getText();
        List<String> sentences = splitTextBySentences(prompt);

        String sourceLang, targetLang;
        if (comboBox.getValue().equals("English To Vietnamese")) {
            sourceLang = "en";
            targetLang = "vi";
        } else {
            sourceLang = "vi";
            targetLang = "en";
        }

        executorService.submit(() -> {
            StringBuilder translatedText = new StringBuilder();
            for (String sentence : sentences) {
                try {
                    String translatedSentence = sendApiRequest(sentence, sourceLang, targetLang);
                    translatedText.append(translatedSentence).append(" ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String finalTranslatedText = translatedText.toString().trim();
            javafx.application.Platform.runLater(() -> outputTextArea.setText(finalTranslatedText));
        });

    }

    public void refresh(){
        inputTextArea.setText("");
        outputTextArea.setText("");
    }
}
