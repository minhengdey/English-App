package com.noface.englishapp.controller;

import com.noface.englishapp.FXMain;
import com.noface.englishapp.view.WordListenGameScreen;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WordListenGameController {
    private WordListenGameScreen screen;
    public WordListenGameController() throws IOException {
        screen = new WordListenGameScreen(this);
    }

    public WordListenGameScreen getScreen() {
        return screen;
    }
    public String sendApiRequestToDicToGetFirstAudio(String text) throws Exception {
        String urlAPI = "https://api.dictionaryapi.dev/api/v2/entries/en/";
        urlAPI += text.toLowerCase();

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

    public String extractAudioUrlsFromJson(String jsonResponse) {
        String result = "";
        JSONArray jsonArray = new JSONArray(jsonResponse);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.has("phonetics")) {
                JSONArray phoneticsArray = obj.getJSONArray("phonetics");
                for (int j = 0; j < phoneticsArray.length(); j++) {
                    JSONObject phonetic = phoneticsArray.getJSONObject(j);
                    if (phonetic.has("audio") && !phonetic.getString("audio").isEmpty()) {
                        result = phonetic.getString("audio");
                    }
                }
            }
        }
        return result;
    }

    public void loadWordsFromSelectedTopic(HashMap<String, String> words, List<String> keys, String topic) throws URISyntaxException {

        if (topic == null || topic.isEmpty()) return;

        words.clear();
        keys.clear();
        URL resourceUrl = FXMain.class.getResource("topic/" + topic + ".txt");
        if (resourceUrl == null) {
            throw new RuntimeException("Resource not found: " + topic);
        }
        File topicFile = new File(resourceUrl.toURI());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(topicFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", 3);
                if (parts.length == 3) {
                    words.put(parts[0].trim(), parts[1].trim() + "\n" + parts[2].trim());
                }
            }
        } catch (Exception ignored) {
        }
        keys.addAll(words.keySet());
        Collections.shuffle(keys);

    }

    public File[] getTopics() {
        URL resourceUrl = FXMain.class.getResource("topic");

        File folder = null;
        try {
            folder = new File(resourceUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        return files;
    }
}
