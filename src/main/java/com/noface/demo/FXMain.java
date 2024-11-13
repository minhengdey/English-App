package com.noface.demo;

import com.noface.demo.Controller.MainController;
import com.noface.demo.card.Card;
import com.noface.demo.Controller.CardLearningController;
import com.noface.demo.screen.CardEditingScreen;
import com.noface.demo.screen.CardLearningScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMain extends Application{

    @Override
    public void init(){
        // context = new SpringApplicationBuilder(SpringMain.class).run();
    }
    public void start(Stage stage) throws Exception {
//        testRandom1(stage);
//        testCardLearningScreen(stage);
//        testTopicScreen(stage);
            testMainScreen(stage);
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
        FXMLLoader loader1 = new FXMLLoader(CardLearningScreen.class.getResource("CardLearningScreen.fxml"));
        FXMLLoader loader2 = new FXMLLoader(CardLearningScreen.class.getResource("CardLearningScreen.fxml"));
//        stage.setScene(new Scene((Parent) loader1.load()));
        Stage stage1 = new Stage();
//        stage1.setScene(new Scene((Parent) loader2.load()));
        loader1.load();
        System.out.println((CardLearningScreen) loader1.getController());
        System.out.println(loader1.getRoot().hashCode());

        loader1.load();
        System.out.println((CardLearningScreen) loader1.getController());
        System.out.println(loader1.getRoot().hashCode());
    }
    public void testRandom3(Stage stage) throws IOException {
    }
    public static void testCardLearningScreen(Stage stage) throws IOException {
        CardLearningController cardLearningController = new CardLearningController();
        stage.setScene(new Scene(cardLearningController.getCardLearningScreen().getRoot()));
        stage.show();
    }
    public void testTopicScreen(Stage stage) throws IOException {
//        CardTopicScreen screen = new CardTopicScreen(ResourceLoader.getInstance().getCardsSampleData());
//        CardEditingScreen screen = new CardEditingScreen();
//        stage.setScene(new Scene(screen.getRoot()));
//        stage.show();

    }
    public void testMainScreen(Stage stage) throws IOException {
        MainController mainController = new MainController();

        stage.setScene(new Scene(mainController.getMainScreen().getRoot()));
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
