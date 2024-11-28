package com.noface.englishapp.controller;

import com.noface.englishapp.FXMain;
import com.noface.englishapp.view.WordCombineGameScreen;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordCombineGameController {
    private WordCombineGameScreen screen;
    private ListProperty<Pair<String, String>> words = new SimpleListProperty(FXCollections.observableArrayList());
    public WordCombineGameController() throws IOException {

        try {
            words.get().clear();
            words.get().addAll(getWordsData());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        screen = new WordCombineGameScreen(this);
    }
    public List<String> extractWordFromJson(String jsonResponse) {
        JSONArray jsonArray = new JSONArray(jsonResponse);
        List<String> words = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.has("word")) {
                String word = obj.getString("word");
                if(word.length() <= 5){
                    words.add(obj.getString("word"));
                }
            }
        }

        return words;
    }
    public String sendApiRequestToDICT_HHDB(String text, String translatePath) throws Exception {
        text = text.toLowerCase();

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


    public WordCombineGameScreen getScreen() {
        return screen;
    }


    public List<Pair<String, String>> getWordsData() throws URISyntaxException {

        URL resourceUrl = FXMain.class.getResource("topic");
        List<Pair<String, String>> words = new ArrayList<>();
        File folder = null;
        try {
            folder = new File(resourceUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        List<String> topics = new ArrayList<>();
        for (File file : files) {
            String topic = file.getName().replace(".txt", "");

            URL topicUrl = FXMain.class.getResource("topic/" + topic + ".txt");
            if (topicUrl == null) {
                throw new RuntimeException("Resource not found: " + topic);
            }
            File topicFile = new File(topicUrl.toURI());

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(topicFile), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";", 3);
                    if (parts.length == 3) {
                        words.add(new Pair<>(parts[0].trim(), topic));
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return words;
    }

    public ObservableList<Pair<String, String>> getWords() {
        return words.get();
    }

    public ListProperty<Pair<String, String>> wordsProperty() {
        return words;
    }

    public void refresh() {
    }
}
