package com.noface.demo.view;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import com.noface.demo.card.Card;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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



}
