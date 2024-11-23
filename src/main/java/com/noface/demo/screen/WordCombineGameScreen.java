package com.noface.demo.screen;

import com.noface.demo.controller.WordCombineGameController;
import com.noface.demo.screen.component.LetterPane;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class WordCombineGameScreen {
    private static final Logger log = LoggerFactory.getLogger(WordCombineGameScreen.class);
    @FXML
    private Text promptText;
    @FXML
    private HBox lettersBox;
    @FXML
    private HBox emptySlotsBox;
    @FXML
    private Text resultText;
    @FXML
    private Button nextButton;
    @FXML
    private Button checkButton;
    @FXML
    private VBox root;
    FXMLLoader loader;

    public WordCombineGameScreen(WordCombineGameController controller) throws IOException {
        words.bind(controller.wordsProperty());
        loader = new FXMLLoader(this.getClass().getResource("WordCombineGameScreen.fxml"));
        loader.setController(this);
        loader.load();

    }

    public <T> T getRoot() {
        return loader.getRoot();
    }

    private ListProperty<String> words = new SimpleListProperty<>(FXCollections.observableArrayList());
    private int currentWordIndex = 0;
    private Map<Integer, StackPane> letterPanes = new HashMap<>();
    private List<StackPane> emptyPanes = new ArrayList<>();
    private List<String> shuffledWordList = new ArrayList<>();

    public void initialize() {

        shuffledWordList.addAll(words.get());
        Collections.shuffle(shuffledWordList);

        nextButton.setOnAction(e -> startNextWord());

        checkButton.setOnAction(e -> checkWord());
        startNextWord();
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
    }

    private void startNextWord() {
        if (currentWordIndex < 2) {
            letterPanes.clear();
            emptyPanes.clear();

            lettersBox.getChildren().clear();
            emptySlotsBox.getChildren().clear();

            String currentWord = shuffledWordList.get(currentWordIndex);
            promptText.setText("Ghép lại từ: " + "_ ".repeat(currentWord.length()));


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
            checkButton.setDisable(false);
        } else {

            promptText.setText("Bạn đã hoàn thành trò chơi!");
            checkButton.setDisable(true);


            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.exit();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    private void removeDraggedLetterOnEmptyBox(String letter) {

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

        String correctWord = shuffledWordList.get(currentWordIndex);
        if (userInput.toString().equals(correctWord)) {
            resultText.setText("Chúc mừng, bạn đã ghép đúng từ!");

            new Thread(() -> {
                try {
                    Thread.sleep(1000);

                    Platform.runLater(() -> {
                        currentWordIndex++;
                        startNextWord();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            resultText.setText("Từ ghép chưa đúng, vui lòng thử lại!");

            restoreDraggedLetters();

        }
    }
}
