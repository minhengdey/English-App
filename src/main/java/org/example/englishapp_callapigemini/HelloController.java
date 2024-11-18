package org.example.englishapp_callapigemini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.stream.Collectors;
import javafx.scene.input.KeyEvent;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class HelloController implements Initializable {
    private ExecutorService executorService;
    private List<String> wordSuggestionsEng;
    private List<String> wordSuggestionsViet;
    private ContextMenu suggestionMenu;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private Button translateButton;

    @FXML
    private WebView outputWebView;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private HBox voiceBox;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputTextArea.setStyle("-fx-border-color: red;");

        comboBox.getItems().addAll("English To Vietnamese", "Vietnamese To English");
        executorService = Executors.newSingleThreadExecutor();
        try {
            wordSuggestionsEng = loadWordSuggestions("english_to_vietnamese");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            wordSuggestionsViet = loadWordSuggestions("vietnamese_to_english");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(wordSuggestionsEng.get(i));
        }
    }


    private List<String> loadWordSuggestions(String language) throws IOException {
        URL urlEng = new URL("http://localhost:8080/" + language);

        HttpURLConnection connEng = (HttpURLConnection) urlEng.openConnection();
        connEng.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connEng.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
            return extractWordFromJson(response.toString());
        } finally {
            connEng.disconnect();
        }
    }

    private List<String> extractWordFromJson(String jsonResponse) {
        JSONArray jsonArray = new JSONArray(jsonResponse);
        List<String> words = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.has("word")) {
                words.add(obj.getString("word"));
            }
        }

        return words;
    }

    @FXML
    private void handleTextInput(KeyEvent event) {
        if (suggestionMenu == null) {
            suggestionMenu = new ContextMenu();
        }
        String text = inputTextArea.getText();
        if (text.isEmpty()) {
            suggestionMenu.hide();
            return;
        }
        List<String> matches;
        if (comboBox.getValue().equals("English To Vietnamese")) {
            matches = wordSuggestionsEng.stream()
                    .filter(word -> word.startsWith(text))
                    .limit(10)
                    .collect(Collectors.toList());
//            for (int i = 0; i < matches.size(); i++) {
//                System.out.println(matches.get(i));
//            }
        } else {
            matches = wordSuggestionsViet.stream()
                    .filter(word -> word.startsWith(text))
                    .limit(10)
                    .collect(Collectors.toList());
        }

        if (matches.isEmpty()) {
            suggestionMenu.hide();
        } else {
            suggestionMenu.getItems().clear();
            for (String match : matches) {
                MenuItem item = new MenuItem(match);
                item.setOnAction(e -> {
                    inputTextArea.setText(match);
                    inputTextArea.positionCaret(inputTextArea.getText().length());
                    suggestionMenu.hide();
                });
                suggestionMenu.getItems().add(item);
            }

            Point2D point = inputTextArea.localToScene(0, inputTextArea.getHeight());

            if (!suggestionMenu.isShowing()) {
                suggestionMenu.show(inputTextArea, point.getX(), point.getY());
            }
        }

    }

    private HashSet<String> sendApiRequestToDic(String text) throws Exception {
        String urlAPI = "https://api.dictionaryapi.dev/api/v2/entries/en/";
        text = text.toLowerCase();
        urlAPI += java.net.URLEncoder.encode(text, StandardCharsets.UTF_8);

        //System.out.println(urlAPI);

        URL url = new URL(urlAPI);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
            return extractAudioUrlsFromJson(response.toString());
        } finally {
            conn.disconnect();
        }
    }

    private HashSet<String> extractAudioUrlsFromJson(String jsonResponse) {
        HashSet<String> audioUrls = new HashSet<>();

        JSONArray jsonArray = new JSONArray(jsonResponse);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.has("phonetics")) {
                JSONArray phoneticsArray = obj.getJSONArray("phonetics");
                for (int j = 0; j < phoneticsArray.length(); j++) {
                    JSONObject phonetic = phoneticsArray.getJSONObject(j);
                    if (phonetic.has("audio") && !phonetic.getString("audio").isEmpty()) {
                        audioUrls.add(phonetic.getString("audio"));
                    }
                }
            }
        }
        return audioUrls;
    }

    @FXML
    private void translateButtonClick(ActionEvent actionEvent) throws Exception {
        String prompt = inputTextArea.getText();

        if (prompt.isEmpty()) {
            showError("Input text cannot be empty.");
            return;
        }

        voiceBox.getChildren().clear();
        HashSet<String> apiAudio = sendApiRequestToDic(prompt);
        List<String> apiAudioList = new ArrayList<>(apiAudio);
        int numberOfButtons = apiAudio.size();

        for (int i = 0; i < numberOfButtons; i++) {
            Button button = getButton(apiAudioList, i);
            voiceBox.getChildren().add(button);
        }
        String translatePath = "";
        if (comboBox.getValue() == null) {
            showError("Please select a translation direction.");
            return;
        } else if (comboBox.getValue().equals("English To Vietnamese")) {
            translatePath = "english_to_vietnamese";
        } else {
            translatePath = "vietnamese_to_english";
        }

        outputWebView.getEngine().loadContent("<p>Translating...</p>");

        String finalTranslatePath = translatePath;
        executorService.submit(() -> {
            String result;
            try {
                result = sendApiRequestToGGTranslate(prompt, finalTranslatePath);
                if (result.isEmpty()) {
                    result = "<p>No result returned from API.</p>";
                }
            } catch (Exception e) {
                result = "<p>Error: Unable to connect to the API.</p>";
                e.printStackTrace();
            }

            String finalResult = result.replaceAll("\\/\\/", "");
            javafx.application.Platform.runLater(() -> {
                outputWebView.getEngine().loadContent(finalResult);
            });
        });
    }

    private Button getButton(List<String> apiAudio, int i) {
        String str = apiAudio.get(i);
        StringBuilder nameButton = new StringBuilder();
        boolean check = false;
        for (int j = 0; j < str.length(); j++) {
            if (str.charAt(j) == '.') {
                check = false;
            }
            if (check) {
                nameButton.append(str.charAt(j));
            }
            if (str.charAt(j) == '-') {
                check = true;
            }

        }
        System.out.println(apiAudio.get(i));
        // Đoạn này thay bằng icon cái loa j đấy đc k nhỉ :>
        if (nameButton.toString().equals("")) {
            nameButton.append("Cái này không có từ của nước nào nè");
        }
        Button button = new Button(nameButton.toString().toUpperCase());
        button.setPrefHeight(translateButton.getPrefHeight());
        button.setPrefWidth(translateButton.getPrefWidth());
        button.setOnAction(e -> {
            try {
                playAudio(apiAudio.get(i));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return button;
    }

    private void playAudio(String audioUrl) {
        try {
            Media media = new Media(audioUrl);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unable to play audio. Please check the format or URL.");
        }
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String sendApiRequestToGGTranslate(String text, String translatePath) throws Exception {
        text = text.toLowerCase();
        // Mã hóa URL để tránh ký tự đặc biệt gây lỗi
        String encodedText = java.net.URLEncoder.encode(text, StandardCharsets.UTF_8);
        String urlStr = String.format("http://localhost:8080/%s/byWord/%s", translatePath, encodedText);

        System.out.println(urlStr);

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
            return extractHtmlFromJson(response.toString());
        } finally {
            conn.disconnect();
        }
    }

    private String extractHtmlFromJson(String jsonResponse) {
        JSONArray jsonArray = new JSONArray(jsonResponse);
        StringBuilder htmlBuilder = new StringBuilder();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.has("html")) {
                htmlBuilder.append(obj.getString("html")).append("<br>");
            }
        }
        return htmlBuilder.toString();
    }

}
