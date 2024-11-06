
package com.noface.demo.card;

import java.io.IOException;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.util.ArrayList;


public class CardLearningUI {
    public final String[] repetitionLabels = {"Again - 1 minutes", "Hard - 6 minutes", "Good - 10 minutes", "Easy - 3 days"};
    public final long[] repetitionTimes = {60, 360, 600, 3 * 24 * 60 * 60};
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
    public void initialize() {
        configureScreenComponent();
    }
    @FXML
    private VBox mainLearningArea;
    @FXML
    private Label doneLearningLabel;
    @FXML
    private Button showAnswerButton;
    @FXML
    private HBox doneButtonBar;
    private List<Button> selectRepetitionButtons = new ArrayList<>();

    private StringProperty frontContent = new SimpleStringProperty();
    private StringProperty backContent = new SimpleStringProperty();
    private BooleanProperty backCardShowed = new SimpleBooleanProperty();
    private CardInteractor interactor;

    public void connect(Card card, CardInteractor interactor) {
        this.interactor = interactor;
        bindProperty(card);
        loadInitializeContent();
    }

    public void bindProperty(Card card) {
        frontContent.bind(card.frontContentProperty());
        backContent.bind(card.backContentProperty());

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

    public void loadInitializeContent() {
        frontView.getEngine().loadContent(frontContent.get());
        backView.getEngine().loadContent("");
    }

    public void configureScreenComponent() {

        for (int i = 0; i < repetitionTimes.length; i++) {
            String label = repetitionLabels[i];
            Button selectRepetitionButton = new Button(label);
            selectRepetitionButtons.add(selectRepetitionButton);
            selectRepetitionButton.setOnAction(getSelectRepetitionButtonEventHandler());
            doneButtonBar.getChildren().add(selectRepetitionButton);
            HBox.setMargin(selectRepetitionButton, new Insets(0, 5, 0, 5));
        }

        cardEditButton.setOnAction(getCardEditButtonEventHandler());
        showAnswerButton.setOnAction(getShowAnswerButtonEventHandler());
    }


    public void clearContentUI() {
        frontView.getEngine().loadContent("");
        backView.getEngine().loadContent("");
    }

    public EventHandler getCardEditButtonEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    interactor.changeToEditCardUI(cardEditButton.getScene());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public EventHandler getShowAnswerButtonEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backCardShowed.set(true);
            }
        };
    }

    public EventHandler getSelectRepetitionButtonEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                interactor.plusCardDueTime(repetitionTimes[selectRepetitionButtons.indexOf(event.getSource())]);
                clearContentUI();
                interactor.setCard(null);
            }

        };
    }

}
