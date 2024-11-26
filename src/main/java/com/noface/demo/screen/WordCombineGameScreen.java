package com.noface.demo.screen;

import com.noface.demo.controller.WordCombineGameController;
import com.noface.demo.screen.component.LetterPane;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordCombineGameScreen {
    private MainScreen mainScreen;
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

    @FXML
    private WebView inforOutput;
    @FXML
    private Button showAnswerButton;

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
        String prompt = shuffledWordList.get(currentWordIndex);



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

    private ListProperty<String> words = new SimpleListProperty<>(FXCollections.observableArrayList());
    private int currentWordIndex = 0;
    private Map<Integer, StackPane> letterPanes = new HashMap<>();
    private List<StackPane> emptyPanes = new ArrayList<>();
    private List<String> shuffledWordList = new ArrayList<>();

    public void initialize() {

        shuffledWordList.addAll(words.get());
        Collections.shuffle(shuffledWordList);

        nextButton.setOnAction(e -> {
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

            String currentWord = shuffledWordList.get(currentWordIndex);
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
                        for(Node node : emptySlotsBox.getChildren()){
                            Pane pane = (Pane) node;
                            if(letterPanes.containsValue(pane) == false){
                                emptySlotsBox.getChildren().set(
                                        emptySlotsBox.getChildren().indexOf(pane),
                                        letterPanes.get(Integer.valueOf(letterPane.hashCode()))
                                );
                                break;
                            }
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
            checkButton.setDisable(false);
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
        String correctWord = shuffledWordList.get(currentWordIndex);
        System.out.println(userInput + " " + correctWord);
        if (userInput.toString().equals(correctWord)) {
            resultText.setText("Congratulations, you have unscrambled the word correctly!");
            try {
                showCurrentWordInfo();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            resultText.setText("The word is not correct, please try again!");

            restoreDraggedLetters();
        }

    }


    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
}
