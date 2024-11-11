package org.example.englishapp_callapigemini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloController implements Initializable {
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
        inputTextArea.setStyle("-fx-border-color: red;");
        outputTextArea.setStyle("-fx-border-color: red;");
        comboBox.getItems().addAll("English To Vietnamese", "Vietnamese To English");
        executorService = Executors.newSingleThreadExecutor();
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
        // Json
//        {
//            "candidates": [
//            {
//                "content": {
//                "parts": [
//                {
//                    "text": "Vào cuối tuần, gia đình tôi rất thích dành thời gian bên nhau. Buổi sáng thứ bảy thường bắt đầu với bữa sáng thịnh soạn tại nhà. Mẹ tôi làm bánh kếp hoặc bánh quế, và tất cả chúng tôi ngồi quanh bàn, trò chuyện về kế hoạch trong ngày. Đôi khi chúng tôi quyết định đi bộ đường dài trong khu rừng gần đó. Nơi đó thật yên bình và xinh đẹp, với những cây cao và tiếng chim hót líu lo. \n"
//                }
//                ],
//                "role": "model"
//            },
//                "finishReason": "STOP",
//                    "index": 0,
//                    "safetyRatings": [
//                {
//                    "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
//                        "probability": "NEGLIGIBLE"
//                },
//                {
//                    "category": "HARM_CATEGORY_HATE_SPEECH",
//                        "probability": "NEGLIGIBLE"
//                },
//                {
//                    "category": "HARM_CATEGORY_HARASSMENT",
//                        "probability": "NEGLIGIBLE"
//                },
//                {
//                    "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
//                        "probability": "NEGLIGIBLE"
//                }
//            ]
//            }
//    ],
//            "usageMetadata": {
//            "promptTokenCount": 77,
//                    "candidatesTokenCount": 104,
//                    "totalTokenCount": 181
//        },
//            "modelVersion": "gemini-1.5-flash-001"
//        }

        int startIndex = response.indexOf("\"text\": \"") + 9;
        int endIndex = response.indexOf("\"", startIndex);
        return response.substring(startIndex, endIndex - 3);

    }

    @FXML
    private void generateContent(ActionEvent actionEvent) {
        String prompt = inputTextArea.getText();
        if (comboBox.getValue().equals("English To Vietnamese")) {
            prompt = "Dịch sang Tiếng Việt " + prompt;
        } else {
            prompt = "Dịch sang Tiếng Anh " + prompt;
        }
        String finalPrompt = prompt;
        executorService.submit(() -> {
            try {
                String response = sendApiRequest(finalPrompt);

                String generatedText = parseResponse(response);
                javafx.application.Platform.runLater(() -> outputTextArea.setText(generatedText));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

}