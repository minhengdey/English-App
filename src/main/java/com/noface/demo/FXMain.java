package com.noface.demo;

import com.noface.demo.card.Card;
import com.noface.demo.card.CardEditingScreen;
import com.noface.demo.card.CardLearningController;
import com.noface.demo.card.CardLearningUI;
import com.noface.demo.resource.ResourceLoader;
import com.noface.demo.topic.CardTopicScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
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
//        testRandom1(stage);
//        testCardLearningUI(stage);
        testTopicScreen(stage);

//        testCardEditingScreen(stage);
//        testRandom3(stage);
    }

    private void testCardEditingScreen(Stage stage) throws IOException {

        Parent root = new CardEditingScreen(new Card("afds", "afsd", "fasdf", "fasdf")).getRoot();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void testRandom1(Stage  stage){
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


        tableView.setItems(cards);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        root.getChildren().add(tableView);
    }
    public void testRandom2(Stage stage) throws IOException {
        FXMLLoader loader1 = new FXMLLoader(CardLearningUI.class.getResource("CardLearningUI.fxml"));
        FXMLLoader loader2 = new FXMLLoader(CardLearningUI.class.getResource("CardLearningUI.fxml"));
//        stage.setScene(new Scene((Parent) loader1.load()));
        Stage stage1 = new Stage();
//        stage1.setScene(new Scene((Parent) loader2.load()));
        loader1.load();
        System.out.println((CardLearningUI) loader1.getController());
        System.out.println(loader1.getRoot().hashCode());

        loader1.load();
        System.out.println((CardLearningUI) loader1.getController());
        System.out.println(loader1.getRoot().hashCode());
    }
    public void testRandom3(Stage stage) throws IOException {
    }
    public static void testCardLearningUI(Stage stage) throws IOException {
        HTMLEditor htmlEditor = new HTMLEditor();
        System.out.println(htmlEditor.getChildrenUnmodifiable());
    }
    public void testTopicScreen(Stage stage) throws IOException {
        CardTopicScreen screen = new CardTopicScreen(ResourceLoader.getInstance().getCardsSampleData());
//        CardEditingScreen screen = new CardEditingScreen();
        stage.setScene(new Scene(screen.getRoot()));
        stage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
