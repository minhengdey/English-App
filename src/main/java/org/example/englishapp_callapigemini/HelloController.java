package org.example.englishapp_callapigemini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
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

    @FXML
    private void translateButtonClick(ActionEvent actionEvent) {
        String prompt = inputTextArea.getText();

        if (prompt.isEmpty()) {
            showError("Input text cannot be empty.");
            return;
        }

        String translatePath = "";
        if (comboBox.getValue() == null) {
            showError("Please select a translation direction.");
            return;
        } else if (comboBox.getValue().equals("English To Vietnamese")) {
            translatePath = "english_to_vietnamese/byWord";
        } else {
            translatePath = "vietnamese_to_english/byWord";
        }

        outputWebView.getEngine().loadContent("<p>Translating...</p>");

        String finalTranslatePath = translatePath;
        executorService.submit(() -> {
            String result;
            try {
                result = sendApiRequest(prompt, finalTranslatePath);
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

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String sendApiRequest(String text, String translatePath) throws Exception {
        String urlStr = String.format(
                "http://localhost:8080/%s/%s",
                translatePath, java.net.URLEncoder.encode(text, StandardCharsets.UTF_8)
        );
        System.out.println(urlStr);
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
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
