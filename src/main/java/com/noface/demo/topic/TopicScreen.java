package com.noface.demo.topic;

import com.noface.demo.card.Card;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.List;


public class TopicScreen {
    @FXML
    private TableView<Card> cardTable;
    @FXML
    private SplitPane rootPane;
    @FXML
    private VBox filterPane;
    @FXML
    private VBox rightOfRootPane;
    @FXML
    private VBox editCardPane;
    @FXML
    private TreeView filterBar;
    private TreeItem<String> topicTree = new TreeItem<>("Topics");
    private TreeItem<String> topicTitleItems = new TreeItem<>("Topics");
    @FXML
    public void initialize() {
        configureScreenComponent();

    }

    public void configureScreenComponent() {
        SplitPane.setResizableWithParent(rightOfRootPane, true);
        SplitPane.setResizableWithParent(filterPane, false);
        SplitPane.setResizableWithParent(editCardPane, false);
        SplitPane.setResizableWithParent(cardTable, true);
        cardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Card, String> sortFieldCol = new TableColumn<>("Sort Field");
        sortFieldCol.setCellValueFactory(new PropertyValueFactory<>("frontContent"));

        TableColumn<Card, String> dueTimeCol = new TableColumn<>("Due time");
        dueTimeCol.setCellValueFactory(new PropertyValueFactory<>("dueTime"));

        cardTable.getColumns().addAll(sortFieldCol, dueTimeCol);
    }

    public void connect(TopicScreenController topicScreenController) {
        filterBar.setShowRoot(false);
        TreeItem<String> treeItem = new TreeItem<>();
        treeItem.setExpanded(true);
        treeItem.getChildren().add(topicTitleItems);
        topicTitleItems.setExpanded(true);
        filterBar.setRoot(treeItem);

        cardTable.setItems(topicScreenController.cardsProperty());


    }
    public void updateTopicTitles(List<TreeItem<String>> titles){
        topicTitleItems.getChildren().clear();
        topicTitleItems.getChildren().addAll(titles);
        for(TreeItem item : topicTitleItems.getChildren()){
            item.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                }
            });
        }
    }
}
