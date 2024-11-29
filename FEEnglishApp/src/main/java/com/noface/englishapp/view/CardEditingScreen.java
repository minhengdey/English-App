package com.noface.englishapp.view;

import com.noface.englishapp.model.Card;
import com.noface.englishapp.controller.TopicScreenController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;

import java.io.IOException;

public class CardEditingScreen {
    private MainScreen mainScreen;
    private StringProperty frontContent;
    private StringProperty backContent;
    private StringProperty cardNameProperty;
    private StringProperty cardTopicProperty;
    @FXML
    private TextField cardNameEditor;
    @FXML
    private HTMLEditor contentEditor;
    @FXML
    private FXMLLoader loader;
    @FXML
    private ComboBox<String> cardSideChoiceBox;
    @FXML
    private TextField cardTopicTextField;
    public CardEditingScreen(Card card) throws IOException {

        loader = new FXMLLoader(this.getClass().getResource("CardEditingScreen.fxml"));
        loader.setController(this);
        loader.load();
        connect(card);
    }
    public CardEditingScreen() throws IOException {
        System.out.println("Here");
        loader = new FXMLLoader(this.getClass().getResource("CardEditingScreen.fxml"));
        loader.setController(this);
        loader.load();
    }
    public CardEditingScreen(TopicScreenController controller){

    }
    public <T> T getRoot() {
        return loader.getRoot();
    }

    @FXML
    void initialize() {
        assert cardSideChoiceBox != null : "fx:id=\"cardSideChoiceBox\" was not injected: check your FXML file 'CardEditingScreen.fxml'.";
        assert contentEditor != null : "fx:id=\"contentEditor\" was not injected: check your FXML file 'CardEditingScreen.fxml'.";
        initScreenComponent();
    }
    public void initScreenComponent(){
        cardSideChoiceBox.getItems().addAll("Front", "Back");
        cardSideChoiceBox.setValue("Front");
    }
    public void connect(Card card) {
        cardSideChoiceBox.setValue("Front");

        frontContent = new SimpleStringProperty();
        backContent = new SimpleStringProperty();
        cardNameProperty = new SimpleStringProperty();
        cardTopicProperty = new SimpleStringProperty();


        cardNameEditor.setText(card.nameProperty().get());


        cardTopicProperty.bindBidirectional(card.topicProperty());
        cardTopicTextField.setText(cardTopicProperty.get());
        cardTopicTextField.textProperty().addListener(observable -> {
            cardTopicProperty.set(cardTopicTextField.getText());
        });

        cardNameEditor.textProperty().addListener((observable, oldValue, newValue) -> {
            cardNameProperty.set(newValue);
        });
        cardNameProperty.bindBidirectional(card.nameProperty());
        frontContent.bindBidirectional(card.frontContentProperty());
        backContent.bindBidirectional(card.backContentProperty());
        contentEditor.setHtmlText(card.frontContentProperty().get());
        contentEditor.setOnKeyReleased(ContentEditorEventHandler());
        for(Node node : contentEditor.lookupAll("ToolBar")){
            node.setOnMouseExited(contenEditorMouseExitHandler());
        }
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
    public void setCardTopicEditable(Boolean b){
        cardTopicTextField.setEditable(b);
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
    public EventHandler<KeyEvent> ContentEditorEventHandler(){
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                updateCardContent();
            }
        };
    }
    public EventHandler<MouseEvent> contenEditorMouseExitHandler(){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                updateCardContent();
            }
        };
    }

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
}
