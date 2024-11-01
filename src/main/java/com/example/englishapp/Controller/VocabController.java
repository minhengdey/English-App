package com.example.englishapp.Controller;

import com.example.englishapp.Vocabulary;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class VocabController implements Initializable {

    @FXML
    AnchorPane anchorPane;
    @FXML
    ImageView imageVocab;
    @FXML
    Label english;
    @FXML
    Label vietnamese;
    private Vocabulary vocabulary;

    public void setData (Vocabulary vocabulary) {
        this.english.setText(vocabulary.getEnglish());
        this.vietnamese.setText(vocabulary.getVietnamese());

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
