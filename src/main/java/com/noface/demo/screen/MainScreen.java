package com.noface.demo.screen;

import com.noface.demo.controller.LoginScreenController;
import com.noface.demo.controller.MainController;
import com.noface.demo.resource.TokenManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class MainScreen {
    private final FXMLLoader loader;
    private MainController mainController;
    private Stage stage;

    private Pane listTopicPane, cardLearningPane, cardTopicPane, translatePane, profilePane, dictionaryPane;

    public MainScreen(MainController mainController, Pane listTopicPane,
                      Pane cardTopicPane, Pane cardLearningPane, Pane translatePane,
                      Pane profilePane, Pane dictionaryPane) throws IOException {
        this.mainController = mainController;
        this.dictionaryPane = dictionaryPane;
        this.listTopicPane = listTopicPane;
        this.cardTopicPane = cardTopicPane;
        this.cardLearningPane = cardLearningPane;
        this.translatePane = translatePane;
        this.profilePane = profilePane;

        loader = new FXMLLoader(this.getClass().getResource("MainScreen.fxml"));
        loader.setController(this);
        loader.load();
    }

    public <T> T getRoot(){
        return loader.getRoot();
    }

    @FXML
    private Button dictionaryButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button topicButton;
    @FXML
    private AnchorPane rightPane;

    @FXML
    private Button translateButton;
    @FXML
    public void initialize() throws IOException {
        configureScreenComponentEvent();
    }
    public void configureScreenComponentEvent(){
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        translateButton.setOnAction(translateButtonClickedEventHandler());
        topicButton.setOnAction(topicButtonClickedEventHanlder());
        profileButton.setOnAction(profileButtonClickedEventHandler());
        logoutButton.setOnAction(logoutButtonClickedEventHandler());
        dictionaryButton.setOnAction(dictionaryButtonClickedEventHandler());
    }

    private EventHandler<ActionEvent> dictionaryButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeToDictionaryScreen();
            }
        };
    }

    private void changeToDictionaryScreen() {
        rightPane.getChildren().clear();
        rightPane.getChildren().add(dictionaryPane);
    }

    private EventHandler<ActionEvent> profileButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeToProfilePane();
            }
        };
    }


    private EventHandler<ActionEvent> topicButtonClickedEventHanlder() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeToListTopicPane();
            }
        };
    }

    private EventHandler<ActionEvent> translateButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeToTranslatePane();
            }
        };
    }

    private EventHandler<ActionEvent> logoutButtonClickedEventHandler()
    {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận đăng xuất");
                alert.setHeaderText("Bạn có chắc chắn muốn đăng xuất không?");
                alert.setContentText("Nhấn OK để đăng xuất, hoặc Cancel để hủy.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) changeToLoginScreen();
            }
        };
    }

    public void changeToListTopicPane(){
        mainController.getTopicScreenController().refreshListTopicTitlesList();
        rightPane.getChildren().clear();
        rightPane.getChildren().add(listTopicPane);
    }
    public void changeToCardTopicPane(String topic){
        mainController.getTopicScreenController().loadCardByTopic(topic);
        rightPane.getChildren().clear();
        rightPane.getChildren().add(cardTopicPane);
    }

    public void changeToTranslatePane() {
        rightPane.getChildren().clear();
        rightPane.getChildren().add(translatePane);
    }
    public void changeToProfilePane(){
        rightPane.getChildren().clear();
        rightPane.getChildren().add(profilePane);
    }

    public void changeToLoginScreen()
    {
        try
        {
            Stage currentStage = (Stage)logoutButton.getScene().getWindow();
            LoginScreenController loginScreenController = new LoginScreenController();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(loginScreenController.getScreen().getRoot()));
            loginStage.setResizable(false);
            //System.out.println (TokenManager.getInstance().getToken());
            TokenManager.getInstance().clearToken();
            loginStage.show();
            currentStage.close();
            //System.out.println (TokenManager.getInstance().getToken());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void changeToMainScreen(){

    }

}
