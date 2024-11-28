package com.noface.englishapp.view;


import com.noface.englishapp.controller.WordListenGameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutorService;


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

        File[] files = controller.getTopics();

        assert files != null;
        topicBox.getItems().clear();
        for (File file : files) {
            topicBox.getItems().add(file.getName().replace(".txt", ""));
        }

        topicBox.setOnAction(event -> {
            try {
                controller.loadWordsFromSelectedTopic(words, keys,
                        topicBox.getSelectionModel().getSelectedItem());
                currentWord = keys.get(0);
                outputTextArea.setText("");
                count = 0;
                genNextWord();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });


    }



    private void genNextWord() {

        if (count < keys.size()) {
            currentWord = keys.get(count);
            count++;
        } else {
            outputTextArea.setText("No more words in this topic. Please choose a new topic or try again.");
            currentWord = null;
        }
    }

    @FXML
    private void pronunciationButtonClick(ActionEvent event) {
        String audioUrl = "";
        try {
            audioUrl = controller.sendApiRequestToDicToGetFirstAudio(currentWord);
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
            ans = "Correct\n" + currentWord + "\n" + words.get(currentWord);
        } else {
            ans = "Incorrect\n";
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
        if(currentWord != null){
            String ans = "Answer: \n" + currentWord + "\n" + words.get(currentWord);
            outputTextArea.setText(ans);
        }
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

    public void refresh() {
        topicBox.getSelectionModel().select(0);
        topicBox.fireEvent(new ActionEvent());
    }
}