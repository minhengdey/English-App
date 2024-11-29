package com.noface.englishapp.view;

import com.noface.englishapp.controller.DictionaryScreenController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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


public class DictionaryScreen implements Initializable {
    private ExecutorService executorService;
    private List<String> wordSuggestionsEng;
    private List<String> wordSuggestionsViet;
    private ContextMenu suggestionMenu;
    private FXMLLoader loader;
    private MainScreen mainScreen;
    private DictionaryScreenController controller;

    public DictionaryScreen(DictionaryScreenController controller) throws IOException {
        this.controller = controller;
        loader = new FXMLLoader(getClass().getResource("DictionaryScreen.fxml"));
        loader.setController(this);
        loader.load();
    }
    public <T> T getRoot(){
        return loader.getRoot();
    }

    @FXML
    private TextField inputTextArea;

    @FXML
    private Button translateButton;

    @FXML
    private WebView outputWebView;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private ComboBox voiceBox;

    @FXML
    private Button audioButton;

    private List<String> apiAudioList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        comboBox.getItems().addAll("English", "Vietnamese");
        comboBox.setValue(comboBox.getItems().get(0));
        executorService = Executors.newSingleThreadExecutor();
        try {
            wordSuggestionsEng = controller.loadWordSuggestions("english_to_vietnamese");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            wordSuggestionsViet = controller.loadWordSuggestions("vietnamese_to_english");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        inputTextArea.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                handleTextInput(event);
            }
        });
        translateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    translateButtonClick(event);
                } catch (Exception e) {
                    System.out.println("Translate error");
                    throw new RuntimeException(e);
                }
            }
        });
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

        if (comboBox.getSelectionModel().getSelectedItem().equals("English")) {
            matches = wordSuggestionsEng.stream()
                    .filter(word -> word.toLowerCase().startsWith(text.toLowerCase()))
                    .limit(10)
                    .collect(Collectors.toList());
        } else {
            matches = wordSuggestionsViet.stream()
                    .filter(word -> word.toLowerCase().startsWith(text.toLowerCase()))
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

            if (!suggestionMenu.isShowing()) {
                suggestionMenu.show(inputTextArea, inputTextArea.localToScreen(0, 0).getX() +
                                inputTextArea.getPadding().getLeft(),
                        inputTextArea.localToScreen(0, 0).getY() + inputTextArea.getHeight());

            }
        }

    }

    private HashSet<String> sendApiRequestToDic(String text) throws Exception {
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


    private void translateButtonClick(ActionEvent actionEvent) throws Exception {
        String prompt = inputTextArea.getText();

        if (prompt.isEmpty()) {
            showError("Input text cannot be empty.");
            return;
        }

        voiceBox.getItems().clear();
        if (comboBox.getValue().equals("English")) {
            HashSet<String> apiAudio = sendApiRequestToDic(prompt);
            apiAudioList = new ArrayList<>(apiAudio);

            for(String audio : apiAudioList) {
                StringBuilder nameButton = new StringBuilder();
                boolean check = false;
                for (int j = 0; j < audio.length(); j++) {
                    if (audio.charAt(j) == '.') {
                        check = false;
                    }
                    if (check) {
                        nameButton.append(audio.charAt(j));
                    }
                    if (audio.charAt(j) == '-') {
                        check = true;
                    }

                }
                voiceBox.getItems().add(nameButton);
            }
            voiceBox.getSelectionModel().select(0);
        }
        String translatePath = "";
        if (comboBox.getValue() == null) {
            showError("Please select a translation direction.");
            return;
        } else if (comboBox.getValue().equals("English")) {
            translatePath = "english_to_vietnamese";
        } else {
            translatePath = "vietnamese_to_english";
        }

        outputWebView.getEngine().loadContent("<p>Translating...</p>");

        String finalTranslatePath = translatePath;
        executorService.submit(() -> {
            String result;
            try {
                result = controller.sendApiRequestToDICT_HHDB(prompt, finalTranslatePath);
                if (result.isEmpty()) {
                    result = "<p>No result returned from API.</p>";
                }
            } catch (Exception e) {
                result = "<p>Error: Unable to connect to the API.</p>";
                e.printStackTrace();
            }

            String finalResult = result.replaceAll("\\/\\/", "");
            Platform.runLater(() -> {
                outputWebView.getEngine().loadContent(finalResult);
            });
        });
    }

    @FXML
    void audioButtonClicked(ActionEvent event) {
        try{
            playAudio(apiAudioList.get(voiceBox.getSelectionModel().getSelectedIndex()));
        }catch(Exception e){
            System.out.println("Audio not selected");
        }
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

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
    public void refresh(){
        outputWebView.getEngine().loadContent("");
        inputTextArea.setText("");
        voiceBox.getItems().clear();
    }
}
