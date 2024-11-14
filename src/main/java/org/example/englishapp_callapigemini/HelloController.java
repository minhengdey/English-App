package org.example.englishapp_callapigemini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloController implements Initializable {
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

        //Call API Gemini
        //private static final String API_KEY = "AIzaSyBeampUMDA65ov5E_UO5XqrR1vZFlssaJI";
        //private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";


//        private String sendApiRequest(String prompt) throws IOException {
//            URL url = new URL(API_URL + "?key=" + API_KEY);
//            HttpURLConnection conn = getHttpURLConnection(prompt, url);
//
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
//                StringBuilder response = new StringBuilder();
//                String responseLine;
//                while ((responseLine = br.readLine()) != null) {
//                    response.append(responseLine.trim());
//                }
//                return response.toString();
//            }
//        }
//        private static HttpURLConnection getHttpURLConnection(String prompt, URL url) throws IOException {
//            HttpURLConnection conn = (HttpURLConnection) url.openconn();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setDoOutput(true);
//
//            String jsonInputString = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }] }";
//            try (OutputStream os = conn.getOutputStream()) {
//                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
//                os.write(input, 0, input.length);
//            }
//            return conn;
//        }
//
//        private String parseResponse(String response) throws IOException {
//            // Json
////        {
////            "candidates": [
////            {
////                "content": {
////                "parts": [
////                {
////                    "text": "Vào cuối tuần, gia đình tôi rất thích dành thời gian bên nhau. Buổi sáng thứ bảy thường bắt đầu với bữa sáng thịnh soạn tại nhà. Mẹ tôi làm bánh kếp hoặc bánh quế, và tất cả chúng tôi ngồi quanh bàn, trò chuyện về kế hoạch trong ngày. Đôi khi chúng tôi quyết định đi bộ đường dài trong khu rừng gần đó. Nơi đó thật yên bình và xinh đẹp, với những cây cao và tiếng chim hót líu lo. \n"
////                }
////                ],
////                "role": "model"
////            },
////                "finishReason": "STOP",
////                    "index": 0,
////                    "safetyRatings": [
////                {
////                    "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
////                        "probability": "NEGLIGIBLE"
////                },
////                {
////                    "category": "HARM_CATEGORY_HATE_SPEECH",
////                        "probability": "NEGLIGIBLE"
////                },
////                {
////                    "category": "HARM_CATEGORY_HARASSMENT",
////                        "probability": "NEGLIGIBLE"
////                },
////                {
////                    "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
////                        "probability": "NEGLIGIBLE"
////                }
////            ]
////            }
////    ],
////            "usageMetadata": {
////            "promptTokenCount": 77,
////                    "candidatesTokenCount": 104,
////                    "totalTokenCount": 181
////        },
////            "modelVersion": "gemini-1.5-flash-001"
////        }
//
//            int startIndex = response.indexOf("\"text\": \"") + 9;
//            int endIndex = response.indexOf("\"", startIndex);
//            return response.substring(startIndex, endIndex - 3);
//
//        }
//        @FXML
//        private void generateContent(ActionEvent actionEvent) {
//            String prompt = inputTextArea.getText();
//            if (comboBox.getValue().equals("English To Vietnamese")) {
//                prompt = "Dịch sang Tiếng Việt " + prompt;
//            } else {
//                prompt = "Dịch sang Tiếng Anh " + prompt;
//            }
//            String finalPrompt = prompt;
//            executorService.submit(() -> {
//                try {
//                    String response = sendApiRequest(finalPrompt);
//
//                    String generatedText = parseResponse(response);
//                    javafx.application.Platform.runLater(() -> outputTextArea.setText(generatedText));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });

    }
}
