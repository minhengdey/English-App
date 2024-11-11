package com.noface.demo.card;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.HTMLEditorSkin;
import javafx.stage.Stage;

    import java.io.IOException;

public class CardEditingScreen {
    private CardInteractor interactor;

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

        loader = new FXMLLoader(this.getClass().getResource("CardEditingUI.fxml"));
        loader.setController(this);
        loader.load();
        connect(card);
    }
    public CardEditingScreen() throws IOException {
        System.out.println("Here");
        loader = new FXMLLoader(this.getClass().getResource("CardEditingUI.fxml"));
        loader.setController(this);
        loader.load();
    }
    public <T> T getRoot() {
        return loader.getRoot();
    }

    @FXML
    void initialize() {
        assert cardSideChoiceBox != null : "fx:id=\"cardSideChoiceBox\" was not injected: check your FXML file 'CardEditingUI.fxml'.";
        assert contentEditor != null : "fx:id=\"contentEditor\" was not injected: check your FXML file 'CardEditingUI.fxml'.";
        initScreenComponent();
    }
    public void initScreenComponent(){
        cardSideChoiceBox.getItems().addAll("Front", "Back");
        cardSideChoiceBox.setValue("Front");

    }
    public void connect(Card card) {

        frontContent = new SimpleStringProperty();
        backContent = new SimpleStringProperty();
        cardNameProperty = new SimpleStringProperty();
        cardTopicProperty = new SimpleStringProperty();

        cardNameProperty.bindBidirectional(card.nameProperty());
        cardNameEditor.setText(card.nameProperty().get());
        cardNameEditor.textProperty().addListener((observable, oldValue, newValue) -> {
            cardNameProperty.set(newValue);
        });

        cardTopicProperty.bindBidirectional(card.topicProperty());
        cardTopicTextField.setText(cardTopicProperty.get());
        cardTopicTextField.textProperty().addListener(observable -> {
            cardTopicProperty.set(cardTopicTextField.getText());
        });


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
    public void connect(Card card , CardInteractor interactor){
        connect(interactor);
        connect(card);
    }
    public void setCardTopicEditable(Boolean b){
        cardTopicTextField.setEditable(b);
    }
    public void connect(CardInteractor interactor){
        this.interactor = interactor;
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
}
