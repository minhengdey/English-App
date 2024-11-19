package org.example.englishapp.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {
    @FXML
    private Text promptText;
    @FXML private HBox lettersBox;
    @FXML private HBox emptySlotsBox;
    @FXML private Text resultText;
    @FXML private Button nextButton;
    @FXML private Button checkButton;

    private List<String> wordList = List.of(
            "abacus", "abandon", "abandoned", "abandonment", "abate", "abbreviation", "abdomen", "abduct", "abduction",
            "abundant", "abuse", "academic", "academy", "accelerate", "accept", "acceptable", "acceptance", "access", "accessible",
            "accident", "accommodation", "accompany", "accomplish", "accord", "account", "accuse", "achieve", "achievement", "acquire",
            "acquisition", "acquit", "acre", "act", "action", "active", "actor", "actress", "actual", "adapt", "addition", "address",
            "admit", "adopt", "advance", "adventure", "advice", "advise", "affect", "affection", "afford", "affordable", "against",
            "agency", "agenda", "aggregate", "aggressive", "agriculture", "alcohol", "alert", "alien", "alike", "alive", "alliance",
            "allocate", "allow", "allowance", "almost", "aloud", "already", "also", "alter", "alternative", "although", "altogether",
            "amazing", "ambition", "amend", "amendment", "amount", "amplify", "analysis", "analyze", "ancient", "anger", "angle", "animal",
            "annual", "answer", "anxiety", "anxious", "apology", "appeal", "appeal", "appear", "appearance", "appetite", "appliance", "apply",
            "appointment", "approve", "aquatic", "area", "arrange", "arrangement", "arrest", "arrival", "article", "artificial", "artist",
            "assert", "assess", "assessment", "asset", "assist", "assistance", "associate", "association", "assume", "assumption", "athlete",
            "attempt", "attention", "attitude", "attract", "attraction", "auction", "audible", "audience", "author", "authority", "automate",
            "automatic", "available", "average", "aversion", "backbone", "bachelor", "balance", "banish", "barrier", "base", "basic",
            "basics", "battery", "beacon", "believe", "benefit", "bicycle", "biology", "blessing", "boast", "bother", "boundary", "bravery",
            "brother", "brutal", "budget", "burden", "cabinet", "capital", "captain", "capture", "careful", "career", "caring", "cattle",
            "celebrate", "censorship", "ceremony", "chaos", "channel", "charity", "charter", "cheap", "check", "chronic", "circuit", "citizen",
            "climate", "clinic", "clothing", "clumsy", "coarse", "collapse", "college", "column", "combine", "comfort", "comment", "commitment",
            "common", "complete", "complex", "comply", "conclude", "concrete", "conference", "contribute", "courage", "create", "current",
            "cushion", "cycle", "daily", "damage", "danger", "daughter", "debate", "decade", "defend", "defender", "delicate", "demand",
            "deprive", "detect", "develop", "device", "dialect", "diet", "difficult", "digital", "dilute", "direct", "disease", "distant",
            "document", "doubt", "dynamic", "dysfunction", "eager", "eagle", "early", "economy", "eclipse", "editor", "education", "effort",
            "elevate", "endure", "enough", "ensure", "escape", "essence", "estate", "estimate", "ethical", "evidence", "example", "examine", "example", "exceed", "exclude", "exclusive", "excuse", "expand", "expect", "experience", "explain", "explore",
            "export", "expose", "express", "extend", "external", "extinct", "extract", "extreme", "eye", "fabric", "famous", "fashion",
            "feature", "federal", "feedback", "festival", "fiction", "figure", "final", "finance", "finish", "fishing", "fitness",
            "flavor", "flexible", "floating", "focus", "follow", "force", "forever", "formal", "formula", "fortune", "freedom", "friend",
            "friendly", "front", "future", "gallery", "gather", "genuine", "giant", "global", "govern", "gossip", "grace", "gradual",
            "graduate", "gravity", "guitar", "happen", "harbor", "harmony", "hazard", "healthy", "hearing", "heart", "height", "heritage",
            "hero", "hesitate", "hockey", "honest", "horizon", "hospital", "host", "hotel", "house", "housing", "human", "humor", "hunger",
            "identity", "ignore", "image", "impact", "import", "impose", "improve", "include", "income", "increase", "independent",
            "indicate", "industry", "influence", "inform", "inspire", "install", "instant", "intact", "interest", "internet", "interval",
            "invest", "invite", "involve", "issue", "jacket", "jazz", "jealous", "joke", "journal", "journey", "judge", "jungle", "junior",
            "justice", "keen", "keyboard", "knowledge", "labor", "language", "laptop", "latter", "laugh", "leader", "learn", "lecture",
            "legend", "library", "lifetime", "limited", "literacy", "literature", "logical", "lounge", "lucky", "luxury", "maintain",
            "manager", "manual", "mansion", "market", "marriage", "massive", "meaning", "measure", "member", "memory", "mention", "merit",
            "message", "method", "mobile", "moment", "morning", "motion", "natural", "navigate", "neither", "nervous", "network", "noble",
            "normal", "notable", "notice", "number", "object", "observe", "office", "online", "option", "origin", "outcome", "outside",
            "overcome", "package", "passion", "patient", "pattern", "peaceful", "penalty", "perform", "permit", "person", "plastic",
            "planet", "player", "police", "popular", "prepare", "privacy", "produce", "project", "promote", "protect", "provide", "publish",
            "purpose", "quality", "quickly", "question", "quota", "reaction", "reality", "receive", "reform", "reject", "release", "remote",
            "replace", "reputation", "request", "rescue", "resign", "resource", "response", "restore", "revenue", "revert", "review", "revolt",
            "reward", "rising", "sample", "season", "second", "select", "serious", "service", "session", "settle", "severe", "shining",
            "sight", "simple", "situation", "society", "source", "special", "speech", "sponsor", "stable", "standard", "station", "student",
            "subject", "success", "suggest", "supply", "surface", "symbol", "system", "target", "team", "television", "tension", "theory",
            "thick", "through", "ticket", "timber", "together", "traffic", "training", "treat", "trust", "unique", "united", "universal",
            "unusual", "vacant", "valley", "victory", "village", "visible", "vocation", "volume", "waiting", "weather", "western", "whisper",
            "window", "witness", "wonder", "workplace", "worthy", "wrist", "yesterday", "youngster", "youthful", "zenith", "zinc");
    private int currentWordIndex = 0;
    private List<StackPane> slotPanes = new ArrayList<>();
    private List<StackPane> draggedLetters = new ArrayList<>();
    private List<String> shuffledWordList = new ArrayList<>();

    public void initialize() {
        shuffledWordList.addAll(wordList);
        Collections.shuffle(shuffledWordList);

        nextButton.setOnAction(e -> startNextWord());

        checkButton.setOnAction(e -> checkWord());


        startNextWord();
    }

    private void startNextWord() {
        if (currentWordIndex < 2) {
            String currentWord = shuffledWordList.get(currentWordIndex);
            promptText.setText("Ghép lại từ: " + "_ ".repeat(currentWord.length()));

            lettersBox.getChildren().clear();
            emptySlotsBox.getChildren().clear();
            slotPanes.clear();
            draggedLetters.clear();


            List<String> letters = new ArrayList<>();
            for (char c : currentWord.toCharArray()) {
                letters.add(String.valueOf(c));
            }
            Collections.shuffle(letters);


            for (String letter : letters) {
                StackPane letterPane = createDraggableLetter(letter);
                lettersBox.getChildren().add(letterPane);
            }


            for (int i = 0; i < currentWord.length(); i++) {
                StackPane emptySlot = createEmptySlot(i);
                emptySlotsBox.getChildren().add(emptySlot);
                slotPanes.add(emptySlot);
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

    private StackPane createDraggableLetter(String letter) {
        Text text = new Text(letter);
        StackPane pane = new StackPane(text);
        pane.setStyle("-fx-padding: 12; -fx-background-color: #9b7ebd; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-size: 15");
        pane.setOnDragDetected(e -> {
            Dragboard db = pane.startDragAndDrop(TransferMode.ANY);
            db.setContent(Collections.singletonMap(DataFormat.PLAIN_TEXT, letter));
            e.consume();
        });
        return pane;
    }

    private StackPane createEmptySlot(int index) {
        StackPane slot = new StackPane();
        Text placeholder = new Text("_");
        slot.getChildren().add(placeholder);
        slot.setStyle("-fx-padding: 20; -fx-border-color: #7e3fb5; -fx-border-width: 3px 3px 3px 3px; -fx-border-radius: 20; -fx-background-color: #ffffff;-fx-background-radius: 22; -fx-font-size: 20");


        slot.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();

            Text text = (Text) slot.getChildren().get(0);
            if (db.hasString() && text.getText().equals("_")) {
                e.acceptTransferModes(TransferMode.COPY);
            }
            e.consume();
        });


        slot.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();

            Text text = (Text) slot.getChildren().get(0);
            if (db.hasString() && text.getText().equals("_")) {
                String draggedLetter = db.getString();
                text.setText(draggedLetter);
                removeDraggedLetter(draggedLetter);
            }
            e.setDropCompleted(true);
            e.consume();
        });

        return slot;
    }

    private void removeDraggedLetter(String letter) {

        for (Node node : lettersBox.getChildren()) {
            if (node instanceof StackPane) {
                StackPane letterPane = (StackPane) node;
                Text text = (Text) letterPane.getChildren().get(0);
                if (text.getText().equals(letter)) {
                    lettersBox.getChildren().remove(letterPane);
                    draggedLetters.add(letterPane);
                    break;
                }
            }
        }
    }

    private void restoreDraggedLetters() {

        for (StackPane letterPane : draggedLetters) {
            lettersBox.getChildren().add(letterPane);
        }
        draggedLetters.clear();
    }

    private void checkWord() {
        StringBuilder userInput = new StringBuilder();
        for (StackPane slot : slotPanes) {
            Text text = (Text) slot.getChildren().get(0);
            userInput.append(text.getText());
        }

        String correctWord =  shuffledWordList.get(currentWordIndex);
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

            for (StackPane slot : slotPanes) {
                Text placeholderText = (Text) slot.getChildren().get(0);
                placeholderText.setText("_");
            }
        }
    }
}
