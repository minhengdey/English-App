package com.noface.demo.screen;


import com.noface.demo.FXMain;
import com.noface.demo.controller.WordCombineGameController;
import com.noface.demo.controller.WordListenGameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;


public class WordListenGameScreen {
    private ExecutorService executorService;
    private String currentWord;
    private HashMap<String, String> words;
    private List<String> keys;
    private int count = 0;
    private FXMLLoader loader;
    private WordListenGameController controller;
    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private ComboBox<String> topicBox;
    private MainScreen mainScreen;


    public WordListenGameScreen(WordListenGameController controller) throws IOException {
        this.controller = controller;
        loader = new FXMLLoader(getClass().getResource("WordListenGame.fxml"));
        loader.setController(this);
        loader.load();
    }

    @FXML
    public void initialize() {

        words = new HashMap<>();
        keys = new ArrayList<>();

        URL resourceUrl = FXMain.class.getResource("topic");

        File folder = null;
        try {
            assert resourceUrl != null;
            folder = new File(resourceUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        assert files != null;
        for (File file : files) {
            topicBox.getItems().add(file.getName().replace(".txt", ""));
        }

        topicBox.setOnAction(event -> {
            try {
                loadWordsFromSelectedTopic();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void loadWordsFromSelectedTopic() throws URISyntaxException {
        String selectedTopic = topicBox.getValue();
        if (selectedTopic == null || selectedTopic.isEmpty()) return;

        words.clear();
        keys.clear();
        URL resourceUrl = FXMain.class.getResource("topic/" + selectedTopic + ".txt");
        if (resourceUrl == null) {
            throw new RuntimeException("Resource not found: " + selectedTopic);
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
        currentWord = keys.get(0);
        outputTextArea.setText("");
        count = 0;
        genNextWord();
    }
    private void genNextWord() {
        if (count < keys.size()) {
            currentWord = keys.get(count);
            count++;
        } else {
            outputTextArea.setText("Hết từ trong chủ đề này. Hãy chọn lại chủ đề hoặc làm lại.");
            currentWord = null;
        }
    }

    @FXML
    private void pronunciationButtonClick(ActionEvent event) {
        String audioUrl = "";
        try {
            audioUrl = sendApiRequestToDicToGetFirstAudio(currentWord);
        } catch (Exception e) {
            System.err.println("Error fetching audio URL: " + e.getMessage());
        }

        if (!audioUrl.isEmpty()) {
            playAudio(audioUrl);
        } else {
            System.out.println("No audio found for the given word.");
        }
    }
    @FXML
    private void checkSpeakAndWriteButtonClick(ActionEvent event) {
        String text = inputTextArea.getText().toLowerCase();
        String ans;
        if (text.equals(currentWord)) {
            ans = "Đúng rồi\n" + currentWord + "\n" + words.get(currentWord);
        } else {
            ans = "Sai rồi\n";
        }
        outputTextArea.setText(ans);

    }
    @FXML
    private void clearForNextWord(ActionEvent event) {
        inputTextArea.clear();
        outputTextArea.clear();
        genNextWord();
    }

    @FXML
    private void displayAnswer(ActionEvent event) {
        String ans = "Đáp án: \n" + currentWord + "\n" + words.get(currentWord);
        outputTextArea.setText(ans);
    }
    private String sendApiRequestToDicToGetFirstAudio(String text) throws Exception {
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

    private String extractAudioUrlsFromJson(String jsonResponse) {
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

    private void playAudio(String audioUrl) {
        try {
            Media media = new Media(audioUrl);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (Exception ignored) {
        }
    }


    public <T> T getRoot() {
        return loader.getRoot();
    }

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
}