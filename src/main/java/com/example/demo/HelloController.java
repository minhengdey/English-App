package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

public class HelloController {

    @FXML
    private TextField vietToEngTextField;

    @FXML
    private TextField engToVietTextField;

    @FXML
    private TextArea vietToEngResultTextArea;

    @FXML
    private TextArea engToVietResultTextArea;

    private Map<String, String> vietEngDictionary = new TreeMap<>();
    private Map<String, String> engVietDictionary = new TreeMap<>();

    @FXML
    public void initialize() {
        loadDictionaries();
    }

    @FXML
    protected void searchVietToEng() {
        String word = vietToEngTextField.getText().trim().toLowerCase();
        System.out.println(word);
        vietToEngResultTextArea.setText(vietEngDictionary.getOrDefault(word, "Từ không tồn tại trong từ điển."));
    }

    @FXML
    protected void searchEngToViet() {
        String word = engToVietTextField.getText().trim().toLowerCase();
        System.out.println(word);
        engToVietResultTextArea.setText(engVietDictionary.getOrDefault(word, "Từ không tồn tại trong từ điển."));
    }

    private void loadDictionaries() {
        loadDictionaryFromFile("/vietanh.txt", vietEngDictionary);
        loadDictionaryFromFile("/anhviet.txt", engVietDictionary);
    }

    private void loadDictionaryFromFile(String filePath, Map<String, String> dictionary) {
        try (InputStream inputStream = getClass().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                throw new IOException("Không thể tìm thấy file từ điển: ");
            }

            String line;
            StringBuilder entry = new StringBuilder();
            String currentWord = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("@")) {
                    if (currentWord != null) {
                        dictionary.put(currentWord, entry.toString().trim());
                    }
                    currentWord = line.substring(1).split(" ")[0].toLowerCase();
                    entry.setLength(0);
                } else if (line.startsWith("*")) {

                } else if (line.startsWith("=")) {
                    entry.append("\nVí dụ: ").append(line.substring(1).trim()).append("\n");
                } else {
                    entry.append(line).append("\n");
                }
            }

            if (currentWord != null) {
                dictionary.put(currentWord, entry.toString().trim());
            }
        } catch (IOException e) {
            vietToEngResultTextArea.setText("Lỗi khi đọc file từ điển: " + e.getMessage());
            engToVietResultTextArea.setText("Lỗi khi đọc file từ điển: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
