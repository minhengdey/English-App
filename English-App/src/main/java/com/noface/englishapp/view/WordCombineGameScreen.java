package com.noface.englishapp.view;

import com.noface.englishapp.controller.WordCombineGameController;
import com.noface.englishapp.view.component.LetterPane;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordCombineGameScreen {
    private MainScreen mainScreen;
    @FXML
    private Label promptText;
    @FXML
    private HBox lettersBox;
    @FXML
    private HBox emptySlotsBox;
    @FXML
    private Label resultText;
    @FXML
    private Button nextButton;
    @FXML
    private Button checkButton;
    @FXML
    private VBox root;

    @FXML
    private WebView inforOutput;
    @FXML
    private Button showAnswerButton;
    @FXML
    private Label topicHintLabel;
    @FXML
    private Label wordCountLabel;
    private int wordPlayed = 0;
    private int wordCorrect = 0;

    private ExecutorService executorService;

    private FXMLLoader loader;

    private WordCombineGameController controller;

    public WordCombineGameScreen(WordCombineGameController controller) throws IOException {
        this.controller = controller;
        words.bind(controller.wordsProperty());
        loader = new FXMLLoader(this.getClass().getResource("WordCombineGameScreen.fxml"));
        loader.setController(this);
        loader.load();

    }
    public void showCurrentWordInfo() throws Exception {
        String prompt = shuffledWordList.get(currentWordIndex).getKey();



        String translatePath = "english_to_vietnamese";
        inforOutput.getEngine().loadContent("<p>Translating...</p>");
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
            javafx.application.Platform.runLater(() -> {
                inforOutput.getEngine().loadContent(finalResult);
            });
        });
    }

    public <T> T getRoot() {
        return loader.getRoot();
    }

    private ListProperty<Pair<String, String>> words = new SimpleListProperty<>(FXCollections.observableArrayList());
    private int currentWordIndex = 0;
    private Map<Integer, StackPane> letterPanes = new HashMap<>();
    private List<StackPane> emptyPanes = new ArrayList<>();
    private List<Pair<String, String>> shuffledWordList = new ArrayList<>();

    public void initialize() {

        for(Pair<String, String> word : words){
            shuffledWordList.add(new Pair<>(word.getKey(), word.getValue()));
        }
        Collections.shuffle(shuffledWordList);

        nextButton.setOnAction(e -> {
            wordPlayed++;
            updateWordCountLabel();
            makeDisappear(resultText, true);
            makeDisappear(showAnswerButton, false);
            makeDisappear(checkButton, false);

            currentWordIndex = (currentWordIndex + 1) % shuffledWordList.size();
            initWord();
        });
        initWord();

        checkButton.setOnAction(e -> checkWord());
        root.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            System.out.println("root drag over");
            if (db.hasString()) {
                e.acceptTransferModes(TransferMode.COPY);
            }
        });
        root.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            System.out.println("root drag dropped");
            if (db.hasString()) {
                Pane pane = letterPanes.get(Integer.valueOf(db.getString()));
                int slotNumber = emptySlotsBox.getChildren().indexOf(pane);
                emptySlotsBox.getChildren().set(slotNumber, emptyPanes.get(slotNumber));
                lettersBox.getChildren().add(pane);
            }
            e.setDropCompleted(true);
        });

        showAnswerButton.setOnAction(e -> {
            try {
                showCurrentWordInfo();
                makeDisappear(resultText, true);
                makeDisappear(showAnswerButton, true);
                makeDisappear(checkButton, true);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        executorService = Executors.newSingleThreadExecutor();
    }

    private void initWord() {
        if (currentWordIndex < words.get().size()) {
            letterPanes.clear();
            emptyPanes.clear();
            inforOutput.getEngine().loadContent("");

            lettersBox.getChildren().clear();
            emptySlotsBox.getChildren().clear();

            String currentWord = shuffledWordList.get(currentWordIndex).getKey();
            topicHintLabel.setText(shuffledWordList.get(currentWordIndex).getValue());
            promptText.setText("Rearrange the word");

            List<String> letters = new ArrayList<>();
            for (char c : currentWord.toCharArray()) {
                letters.add(String.valueOf(c));
            }
            Collections.shuffle(letters);


            for (String letter : letters) {
                LetterPane letterPane = new LetterPane(letter);
                lettersBox.getChildren().add(letterPane);
                letterPanes.put(letterPane.hashCode(), letterPane);

                letterPane.getStyleClass().add("empty-letter-pane");
                letterPane.setOnDragDetected(e -> {
                    Dragboard db = letterPane.startDragAndDrop(TransferMode.ANY);
                    db.setContent(Collections.singletonMap(DataFormat.PLAIN_TEXT, String.valueOf(letterPane.hashCode())));
                    e.consume();
                });
                letterPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        LetterPane letterPane = (LetterPane) event.getSource();
                        boolean moveValid = false;
                        if(emptySlotsBox.getChildren().contains(letterPane) == false) {
                            for(Node node : emptyPanes){
                                Pane pane = (Pane) node;
                                System.out.println("Log letter pane");
                                if(emptySlotsBox.getChildren().contains(pane)){

                                    emptySlotsBox.getChildren().set(
                                            emptyPanes.indexOf(pane),
                                            letterPane
                                    );
                                    moveValid = true;
                                    break;
                                }
                            }
                        }
                        System.out.println(emptySlotsBox.getChildren());

                        if(moveValid == false && emptySlotsBox.getChildren().contains(letterPane)){
                            System.out.println("Move failed");
                            int emptySlotIndex = emptySlotsBox.getChildren().indexOf(letterPane);
                            emptySlotsBox.getChildren().set(emptySlotIndex, emptyPanes.get(emptySlotIndex));
                            lettersBox.getChildren().add(letterPane);
                        }
                        event.consume();
                    }
                });

            }


            for (int i = 0; i < currentWord.length(); i++) {
                LetterPane emptyPane = new LetterPane("_");
                emptyPane.getStyleClass().add("empty-letter-pane");
                emptyPanes.add(emptyPane);

                emptyPane.setOnDragOver(e -> {
                    Dragboard db = e.getDragboard();
                    if (db.hasString()) {
                        e.acceptTransferModes(TransferMode.COPY);
                    }
                    e.consume();
                });

                emptyPane.setOnDragDropped(e -> {
                    Dragboard db = e.getDragboard();
                    int oldSlotNumber = emptySlotsBox.getChildren().indexOf(
                            letterPanes.get(Integer.valueOf(db.getString())));
                    if(oldSlotNumber == -1){
                        emptySlotsBox.getChildren().set(
                                emptySlotsBox.getChildren().indexOf(emptyPane),
                                letterPanes.get(Integer.valueOf(db.getString()))
                        );
                    }else{
                        emptySlotsBox.getChildren().set(oldSlotNumber, emptyPanes.get(oldSlotNumber));
                        emptySlotsBox.getChildren().set(
                                emptySlotsBox.getChildren().indexOf(emptyPane),
                                letterPanes.get(Integer.valueOf(db.getString()))
                        );
                    }

                    e.setDropCompleted(true);
                    e.consume();
                });



                emptySlotsBox.getChildren().add(emptyPane);
            }

            resultText.setText("");
            makeDisappear(checkButton, false);
            makeDisappear(showAnswerButton, false);
        }
    }

    private void restoreDraggedLetters() {
        lettersBox.getChildren().clear();
        emptySlotsBox.getChildren().clear();

        for (Pane pane : letterPanes.values()) {
            lettersBox.getChildren().add(pane);
        }

        for (StackPane pane : emptyPanes) {
            emptySlotsBox.getChildren().add(pane);
        }
    }

    private void checkWord() {
        StringBuilder userInput = new StringBuilder();
        for (Node pane : emptySlotsBox.getChildren()) {
            LetterPane letterPane = (LetterPane) pane;

            userInput.append(letterPane.getLetter());
        }
        String correctWord = shuffledWordList.get(currentWordIndex).getKey();
        System.out.println(userInput + " " + correctWord);
        if (userInput.toString().equals(correctWord)) {
            makeDisappear(resultText, false);
            resultText.setStyle("-fx-text-fill: #22af13");
            resultText.setText("Congratulations, you have unscrambled the word correctly!");
            makeDisappear(checkButton, true);
            makeDisappear(showAnswerButton, true);
            wordCorrect++;
            try {
                showCurrentWordInfo();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            makeDisappear(resultText, false);
            resultText.setStyle("-fx-text-fill: red;");
            resultText.setText("The word is not correct, please try again!");
            restoreDraggedLetters();
        }

    }

    public void refresh(){
        wordPlayed = 0;
        wordCorrect = 0;
        updateWordCountLabel();
        Collections.shuffle(shuffledWordList);
        currentWordIndex = 0;
        makeDisappear(resultText, true);
        makeDisappear(showAnswerButton, false);
        makeDisappear(checkButton, false);
        makeDisappear(nextButton, false);
        initWord();
    }
    public void makeDisappear(Node node, boolean b){
        node.setVisible(!b);
        node.setManaged(!b);
    }
    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
    public void updateWordCountLabel(){
        wordCountLabel.setText(String.format("%d/%d", wordCorrect, wordPlayed));
    }
}
