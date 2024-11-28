
package com.noface.englishapp.view;

import java.io.IOException;
import java.util.List;

import com.noface.englishapp.model.Card;
import com.noface.englishapp.controller.CardLearningInteractor;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;


public class CardLearningScreen {

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
    private MainScreen mainScreen;


    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

    private FXMLLoader loader;
    public <T> T getRoot(){
        return loader.getRoot();
    }

    public CardLearningScreen() throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("CardLearningScreen.fxml"));
        loader.setController(this);
        loader.load();
    }
    private final StringProperty frontContent = new SimpleStringProperty();
    private final StringProperty backContent = new SimpleStringProperty();
    private final BooleanProperty cardToLearnAvailabled = new SimpleBooleanProperty();
    public CardLearningScreen(CardLearningInteractor interactor) throws IOException {
        this();
        this.interactor = interactor;
        backContent.bind(interactor.backContentPropertyProperty());
        frontContent.bind(interactor.frontContentPropertyProperty());
        cardToLearnAvailabled.bind(interactor.cardAvailabledProperty());
        cardToLearnAvailabled.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                handleCardToLearnAvailabledStatusChange(observable, oldValue, newValue);
            }
        });
        handleBackCardShowedChange(cardToLearnAvailabled, true, true);

    }

    private void handleCardToLearnAvailabledStatusChange(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if(newValue == true){
            mainLearningArea.setVisible(true);
            doneLearningLabel.setVisible(false);
        }else{
            mainLearningArea.setVisible(false);
            doneLearningLabel.setVisible(true);
        }
    }

    @FXML
    public void initialize(){
        addCustomScreenComponent();
        configureScreenComponentEvent();
    }



    private CardLearningInteractor interactor;

    private Queue<Card> cards = new PriorityQueue<>(Card.comparatorByDueTimeNearest());
    private ListProperty<Card> cardListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());



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
    private final BooleanProperty backCardShowed = new SimpleBooleanProperty();
    public final long[] repetitionTimes = {60, 360, 600, 3 * 24 * 60 * 60};
    public void configureScreenComponentEvent() {
        for (int i = 0; i < repetitionTimes.length; i++) {
            Button button = selectRepetitionButtons.get(i);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    handleRepititionSelectButtonClicked(event);
                }
            });
        }


        showAnswerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleShowAnswerButtonClicked(event);
            }
        });


        backCardShowed.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                handleBackCardShowedChange(observable, oldValue, newValue);
            }
        });
        backCardShowed.set(true);

        cardEditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                handleEditButtonClicked(actionEvent);
            }
        });
    }
    public void startShowing(){
        if(cardToLearnAvailabled.get() == false){
            handleCardToLearnAvailabledStatusChange(cardToLearnAvailabled, false, false);
        }else{
            backCardShowed.set(false);
            handleCardToLearnAvailabledStatusChange(cardToLearnAvailabled, true, true);
            handleBackCardShowedChange(backCardShowed, false, false);
        }
    }
    public void handleBackCardShowedChange(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue){
        if(newValue.equals(true)){
            showAnswerButton.setVisible(false);
            doneButtonBar.setVisible(true);
            frontView.getEngine().loadContent(frontContent.get());
            backView.getEngine().loadContent(backContent.get());
        }else{
            showAnswerButton.setVisible(true);
            doneButtonBar.setVisible(false);
            frontView.getEngine().loadContent(frontContent.get());
            backView.getEngine().loadContent("");
        }
    }
    public void handleEditButtonClicked(ActionEvent event){
        Stage stage = new Stage();
        try {
            CardEditingScreen screen = new CardEditingScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(screen.getRoot()));
            screen.connect(interactor.getCurrentCard());
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    interactor.saveEditedCard(interactor.getCurrentCard());
                    startShowing();
                }
            });
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void handleShowAnswerButtonClicked(ActionEvent event) {
        backCardShowed.set(true);
    }
    public void handleRepititionSelectButtonClicked(ActionEvent event) {
        interactor.plusCardDueTime(repetitionTimes[selectRepetitionButtons.indexOf(event.getSource())]);
        interactor.changeToNextCard();
        backCardShowed.set(false);
    }

}
