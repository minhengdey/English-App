package com.noface.demo.screen;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noface.demo.resource.TokenManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DictionaryScreen {
    private FXMLLoader loader;
    private MainScreen mainScreen;

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

    public DictionaryScreen() throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("DictionaryScreen.fxml"));
        loader.setController(this);
        loader.load();
    }

    public <T> T getRoot() {
        return loader.getRoot();
    }

    private ExecutorService executorService;
    private List<String> wordSuggestionsEng;
    private List<String> wordSuggestionsViet;
    private ContextMenu suggestionMenu;

    @FXML
    private TextField inputTextArea;

    @FXML
    private Button translateButton;

    @FXML
    private WebView outputWebView;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private HBox voiceBox;

    public void initialize() {
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

        inputTextArea.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                handleTextInput(keyEvent);
            }
        });

        translateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    handleTranslateButtonClicked(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }


    private List<String> loadWordSuggestions(String language) throws IOException {
        URL urlEng = new URL("http://localhost:8080/" + language);

        HttpURLConnection connEng = (HttpURLConnection) urlEng.openConnection();
        connEng.setRequestMethod("GET");
        connEng.setRequestProperty("Authorization", "Bearer " + TokenManager.getInstance().getToken());

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
        urlAPI += text.toLowerCase();


        URL url = new URL(urlAPI);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + TokenManager.getInstance().getToken());
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
    private void handleTranslateButtonClicked(ActionEvent actionEvent) throws Exception {
        String prompt = inputTextArea.getText();

        if (prompt.isEmpty()) {
            showError("Input text cannot be empty.");
            return;
        }

//        voiceBox.getChildren().clear();
//        HashSet<String> apiAudio = sendApiRequestToDic(prompt);
//        List<String> apiAudioList = new ArrayList<>(apiAudio);
//        int numberOfButtons = apiAudio.size();
//
//        for (int i = 0; i < numberOfButtons; i++) {
//            Button button = getButton(apiAudioList, i);
//            voiceBox.getChildren().add(button);
//        }
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
                result = sendApiRequestToDICT_HHDB(prompt, finalTranslatePath);
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

    private HttpClient httpClient = HttpClient.newHttpClient();

    private String sendApiRequestToDICT_HHDB(String text, String translatePath) throws Exception {
        text = text.toLowerCase();

        String encodedText = java.net.URLEncoder.encode(text, StandardCharsets.UTF_8);
        String urlStr = String.format("http://localhost:8080/%s/byWord/%s", translatePath, encodedText);

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + TokenManager.getInstance().getToken());


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
