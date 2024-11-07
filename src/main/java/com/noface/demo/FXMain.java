package com.noface.demo;

import com.noface.demo.card.Card;
import com.noface.demo.card.CardLearningController;
import com.noface.demo.card.CardLearningUI;
import com.noface.demo.topic.TopicScreenController;
import com.noface.demo.topic.TopicScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMain extends Application{
    private ConfigurableApplicationContext context;

    @Override
    public void init(){
        // context = new SpringApplicationBuilder(SpringMain.class).run();
    }
    public void start(Stage stage) throws Exception {
//        testRandom(stage);
        testCardLearningUI(stage);
//        testTopicScreen(stage);
    }
    public void testRandom(Stage  stage){
        VBox root = new VBox();
        stage.setScene(new Scene(root));
        stage.show();
        TableView<Card> tableView = new TableView();
        tableView.setEditable(true);
        TableColumn<Card, String> firstCol = new TableColumn("First");
        firstCol.setCellValueFactory(new PropertyValueFactory<Card, String>("frontContent"));
        TableColumn<Card, String> secondCol = new TableColumn("Second");
        secondCol.setCellValueFactory(new PropertyValueFactory<Card, String>("backContent"));
        tableView.getColumns().addAll(firstCol, secondCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ObservableList<Card> cards = FXCollections.observableArrayList();

        // Data
        cards.add(new Card("1", "2", "3"));
        cards.add(new Card("4", "5", "6"));
        tableView.setItems(cards);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        root.getChildren().add(tableView);

    }
    public static void testCardLearningUI(Stage stage) throws IOException {
        System.out.println("This is test card learning UI");
        FXMLLoader loader = new FXMLLoader(CardLearningUI.class.getResource("CardLearningUI.fxml"));
        // FXMLLoader loader = new FXMLLoader(BrowseUI.class.getResource("BrowseUI.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        CardLearningController cardLearningController = new CardLearningController(loader);
    }
    public void testTopicScreen(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(TopicScreen.class.getResource("TopicScreen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        TopicScreenController controller = new TopicScreenController(loader.getController());
    }
    public static void main(String[] args) {
        launch(args);
    }
}
