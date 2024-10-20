package com.noface.demo.view;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import com.noface.demo.model.Card;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.web.HTMLEditor;



public class BrowseUI {
    private ArrayList<Card> cards;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private AnchorPane basePane;

    @FXML
    private TableView tableView;

    @FXML
    private FlowPane toolBar;

    @FXML
    private TreeView<?> treeView;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private HTMLEditor cardEditor;

    @FXML
    private ChoiceBox fieldDropdown;
    private Card currentCard;


    @FXML
    void initialize() throws FileNotFoundException {

        createTreeView();
        configUI();

        configTable();
        configCardEditorBehavior();
    }
    private void configUI(){
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
    }

    private void createTreeView(){
        System.out.println("Here");
        TreeItem item = new TreeItem<String>("hehe");
        treeView.setRoot(item);
        TreeItem<String> subItem = new TreeItem<>("hoho");
        item.getChildren().add(subItem);

    }


    private void configCardEditorBehavior(){
        cardEditor.addEventHandler(InputEvent.ANY, new EventHandler<InputEvent>() {
            @Override
            public void handle(InputEvent event) {
                if(currentCard != null){
                    currentCard.getFields().set(fieldDropdown.getSelectionModel().getSelectedIndex(),
                            cardEditor.getHtmlText()) ;
                }
            }
        });

    }

    public void updateCards(List<Card> cards){
        tableView.setItems(FXCollections.observableList(cards));
    }
    private void configTable(){
        TableColumn<Card, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Card, String> dueDateCol = new TableColumn<>("Due date");
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        tableView.getColumns().addAll(idCol, dueDateCol);
        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currentCard = (Card) tableView.getSelectionModel().getSelectedItem();

                if(currentCard != null){

                    fieldDropdown.getItems().clear();
                    int idx = 0;
                    for(String s : currentCard.getFields()){
                        String menuItem = String.valueOf(idx);
                        fieldDropdown.getItems().add(menuItem);
                        idx ++;
                    }
                    if(currentCard.getFields().size() != 0){
                        cardEditor.setHtmlText(currentCard.getFields().get(0));
                        fieldDropdown.setValue(fieldDropdown.getItems().get(0));
                        fieldDropdown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                if((int) newValue == -1){
                                    newValue = 0;
                                }
                                cardEditor.setHtmlText(currentCard.getFields().get((int) newValue));
                            }
                        });
                    }

                }


            }
        });

    }
}
