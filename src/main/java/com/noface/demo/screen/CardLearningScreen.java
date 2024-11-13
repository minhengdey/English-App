
package com.noface.demo.screen;

import java.io.IOException;
import java.util.List;

import com.noface.demo.card.Card;
import com.noface.demo.Controller.CardInteractor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.ArrayList;


public class CardLearningScreen {

    // Component list.
    @FXML
    private VBox root;
    @FXML
    private WebView frontView;
    @FXML
    private WebView backView;
    @FXML
    private Button cardEditButton;
    @FXML
    private VBox mainLearningArea;
    @FXML
    private Label doneLearningLabel;
    @FXML
    private Button showAnswerButton;
    @FXML
    private HBox doneButtonBar;
    @FXML
    private Label cardNameLabel;
    private FXMLLoader loader;
    public <T> T getRoot(){
        return loader.getRoot();
    }

    public CardLearningScreen() throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("CardLearningScreen.fxml"));
        loader.setController(this);
        loader.load();
    }
    @FXML
    public void initialize(){
        addCustomScreenComponent();
    }

    public void connect(Card card, CardInteractor interactor) {
        this.interactor = interactor;
        configureScreenComponentEvent(card, interactor);
        loadInitializeContent();
    }


    private final BooleanProperty backCardShowed = new SimpleBooleanProperty();
    private CardInteractor interactor;
    public void bindInteractorProperty(CardInteractor interactor){
        showAnswerButton.visibleProperty().bind(Bindings.createBooleanBinding(()->{
            return !backCardShowed.get() && interactor.getCardAvailabled();
        }, backCardShowed, interactor.cardAvailabledProperty()));

        doneButtonBar.visibleProperty().bind(Bindings.createBooleanBinding(()->{
            return backCardShowed.get() && interactor.getCardAvailabled();
        }, backCardShowed, interactor.cardAvailabledProperty()));

        cardEditButton.visibleProperty().bind(interactor.cardAvailabledProperty());
        mainLearningArea.visibleProperty().bind(interactor.cardAvailabledProperty());
        doneLearningLabel.visibleProperty().bind(interactor.cardAvailabledProperty().not());
    }

    private final StringProperty frontContent = new SimpleStringProperty();
    private final StringProperty backContent = new SimpleStringProperty();
    public void bindCardProperty(Card card) {
        frontContent.bind(card.frontContentProperty());
        backContent.bind(card.backContentProperty());
        cardNameLabel.textProperty().bind(card.nameProperty());

        backCardShowed.set(true);
        backCardShowed.addListener((observable, oldValue, newValue) -> {
            if (backCardShowed.get()) {
                frontView.getEngine().loadContent(frontContent.get());
                backView.getEngine().loadContent(backContent.get());
            } else {
                frontView.getEngine().loadContent(frontContent.get());
                backView.getEngine().loadContent("");
            }
        });
        backCardShowed.set(false);

        frontContent.addListener((observable, oldValue, newValue) -> {
            frontView.getEngine().loadContent(frontContent.get());
        });

        backContent.addListener((observable, oldValue, newValue) -> {
            if (backCardShowed.get()) {
                backView.getEngine().loadContent(backContent.get());
            } else {
                backView.getEngine().loadContent("");
            }
        });


    }

    public void loadInitializeContent() {
        frontView.getEngine().loadContent(frontContent.get());
        backView.getEngine().loadContent("");

    }

    private List<Button> selectRepetitionButtons = new ArrayList<>();
    public final String[] repetitionLabels = {"Again - 1 minutes", "Hard - 6 minutes", "Good - 10 minutes", "Easy - 3 days"};
    public void addCustomScreenComponent(){
        for (int i = 0; i < repetitionTimes.length; i++) {
            String label = repetitionLabels[i];
            Button selectRepetitionButton = new Button(label);
            selectRepetitionButtons.add(selectRepetitionButton);
            doneButtonBar.getChildren().add(selectRepetitionButton);
            HBox.setMargin(selectRepetitionButton, new Insets(0, 5, 0, 5));
        }
    }

    public final long[] repetitionTimes = {60, 360, 600, 3 * 24 * 60 * 60};
    public void configureScreenComponentEvent(Card card, CardInteractor interactor) {
        bindInteractorProperty(interactor);
        bindCardProperty(card);
        for (int i = 0; i < repetitionTimes.length; i++) {
            Button button = selectRepetitionButtons.get(i);
            button.setOnAction(selectRepetitionButtonEventHandler());
        }
        cardEditButton.setOnAction(cardEditButtonEventHandler());
        showAnswerButton.setOnAction(showAnswerButtonEventHandler());
    }


    public EventHandler<ActionEvent> cardEditButtonEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage stage = (Stage) cardEditButton.getScene().getWindow();

                    Stage editStage = interactor.openEditingStage(stage);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public EventHandler<ActionEvent> showAnswerButtonEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backCardShowed.set(true);
            }
        };
    }

    public EventHandler<ActionEvent> selectRepetitionButtonEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                interactor.plusCardDueTime(repetitionTimes[selectRepetitionButtons.indexOf(event.getSource())]);
                interactor.setCard(null);
            }
        };
    }

}
