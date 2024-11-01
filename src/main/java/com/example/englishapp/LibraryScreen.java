package com.example.englishapp;

import com.example.englishapp.Controller.VocabularyController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LibraryScreen implements Initializable {

    @FXML
    TextField searchBox;
    @FXML
    Button searchButton;
    @FXML
    Button deleteButton;
    @FXML
    ImageView imageVocab;
    @FXML
    TextField englishBox;
    @FXML
    TextField vietnameseBox;
    @FXML
    Button saveButton;
    @FXML
    Label username;
    @FXML
    ImageView avata;
    @FXML
    AnchorPane add;
    @FXML
    AnchorPane deleteAll;
    @FXML
    GridPane gridPaneVocab;

    private Vocabulary vocabulary;

    private VocabularyController vocabularyController;

    private List<Vocabulary> listVocab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.listVocab = vocabularyController.getList();
        int row = 0, column = 0;
        for (Vocabulary i : listVocab) {
            gridPaneVocab.add(i, column++, row);
        }
    }

    public void ADD () {
        vocabularyController.addWord();
    }


}
