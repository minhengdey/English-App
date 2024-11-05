package com.noface.demo.card;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class CardEditingUI {
    private CardInteractor interactor;

    private StringProperty frontContent = new SimpleStringProperty();
    private StringProperty backContent = new SimpleStringProperty();
    @FXML
    private HTMLEditor contentEditor;
    @FXML
    private Button backButton;
    @FXML
    private ChoiceBox<String> cardSideChoiceBox;

    @FXML
    void initialize() {
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'CardEditingUI.fxml'.";
        assert cardSideChoiceBox != null : "fx:id=\"cardSideChoiceBox\" was not injected: check your FXML file 'CardEditingUI.fxml'.";
        assert contentEditor != null : "fx:id=\"contentEditor\" was not injected: check your FXML file 'CardEditingUI.fxml'.";
        initScreenComponent();
    }
    public void initScreenComponent(){
        cardSideChoiceBox.getItems().addAll("Front", "Back");
        cardSideChoiceBox.setValue("Front");
        cardSideChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if(newValue.equals("Front")){
                    contentEditor.setHtmlText(frontContent.get());
                }else{
                    contentEditor.setHtmlText(backContent.get());
                }
            }
        });
    }
    public void setup(Card card, CardInteractor interactor) {
        this.interactor = interactor;
        frontContent.bindBidirectional(card.frontContentProperty());
        backContent.bindBidirectional(card.backContentProperty());
        contentEditor.setHtmlText(frontContent.get());
        contentEditor.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                updateCardContent();
            }
        });
        contentEditor.setOnInputMethodTextChanged(new EventHandler<InputEvent>() {
            @Override
            public void handle(InputEvent event) {
                updateCardContent();
            }
        });


        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(interactor.changeToPreviousScene(backButton.getScene()));
            }
        });
    }
    public void updateCardContent(){
        String content = contentEditor.getHtmlText();
        content = String.join("", content.split("contenteditable=\"true\""));
        System.out.println(content);
        if(cardSideChoiceBox.getSelectionModel().getSelectedItem().equals("Front")){
            frontContent.set(content);
        }else{
            backContent.set(content);
        }
    }
}
