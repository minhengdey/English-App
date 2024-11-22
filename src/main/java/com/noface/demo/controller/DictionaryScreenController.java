package com.noface.demo.controller;

import com.noface.demo.resource.TokenManager;
import com.noface.demo.screen.DictionaryScreen;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DictionaryScreenController {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ListProperty<String> wordSuggestionsEng = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<String> wordSuggestionsViet = new SimpleListProperty<>(FXCollections.observableArrayList());
    private DictionaryScreen screen;
    public DictionaryScreenController() throws IOException {
        screen = new DictionaryScreen(this);
    }
    public DictionaryScreen getScreen(){
        return screen;
    }
    public void refresh(){
        executorService = Executors.newSingleThreadExecutor();
        try {
            wordSuggestionsEng.clear();
            wordSuggestionsEng.addAll(loadWordSuggestions("english_to_vietnamese"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            wordSuggestionsEng.clear();
            wordSuggestionsViet.addAll(loadWordSuggestions("vietnamese_to_english"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public String getWordDictionaryData(String prompt, String finalTranslatePath){
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
            return finalResult;

        });
        return "";
    }
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

    public ObservableList<String> getWordSuggestionsEng() {
        return wordSuggestionsEng.get();
    }

    public ListProperty<String> wordSuggestionsEngProperty() {
        return wordSuggestionsEng;
    }

    public ObservableList<String> getWordSuggestionsViet() {
        return wordSuggestionsViet.get();
    }

    public ListProperty<String> wordSuggestionsVietProperty() {
        return wordSuggestionsViet;
    }
}
