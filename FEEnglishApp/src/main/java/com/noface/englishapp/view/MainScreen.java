package com.noface.englishapp.view;

import com.noface.englishapp.controller.MainController;
import com.noface.englishapp.utils.TokenManager;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Optional;

public class MainScreen {
    private final FXMLLoader loader;
    private MainController mainController;
    private Stage stage;

    private Pane listTopicPane, cardLearningPane, cardTopicPane, translatePane,
            profilePane, dictionaryPane, wordCombinGamePane, wordListenGamePane,
            gameScreenPane;
    @FXML
    private StackPane imageContainer;
    private LoginScreen loginScreen;
    @FXML
    private VBox leftVBox;



    public MainScreen(MainController mainController, Pane listTopicPane,
                      Pane cardTopicPane, Pane cardLearningPane, Pane translatePane,
                      Pane profilePane, Pane dictionaryPane, Pane wordCombineGamePane,
                      Pane wordListenGamePane, Pane gameScreenPane) throws IOException {
        this.mainController = mainController;
        this.dictionaryPane = dictionaryPane;
        this.listTopicPane = listTopicPane;
        this.cardTopicPane = cardTopicPane;
        this.cardLearningPane = cardLearningPane;
        this.translatePane = translatePane;
        this.profilePane = profilePane;
        this.wordCombinGamePane = wordCombineGamePane;
        this.wordListenGamePane = wordListenGamePane;
        this.gameScreenPane = gameScreenPane;


        setAnchor(listTopicPane);
        setAnchor(cardLearningPane);
        setAnchor(cardTopicPane);
        setAnchor(translatePane);
        setAnchor(profilePane);
        setAnchor(dictionaryPane);
        setAnchor(wordCombinGamePane);
        setAnchor(wordListenGamePane);
        setAnchor(gameScreenPane);


        loader = new FXMLLoader(this.getClass().getResource("MainScreen.fxml"));
        loader.setController(this);
        loader.load();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("keep.png")));
        ImageView imageView = new ImageView(image);
        imageContainer.getChildren().add(imageView);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(50);
        leftVBox.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                imageView.setFitWidth(150);
            }
        });
        leftVBox.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                imageView.setFitWidth(50);
            }
        });
    }

    public <T> T getRoot(){
        return loader.getRoot();
    }
    private Button currentButton;
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
    private Button gameButton;
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
        gameButton.setOnAction(gameButtonClickedEventHandler());
    }

    private EventHandler<ActionEvent> gameButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetButton(event);
                changeToGameScreen();
            }
        };
    }

    private EventHandler<ActionEvent> dictionaryButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetButton(event);
                changeToDictionaryScreen();
            }
        };
    }

    private void changeToGameScreen() {
        rightPane.getChildren().clear();
        rightPane.getChildren().add(gameScreenPane);
    }

    public void changeToDictionaryScreen() {
        rightPane.getChildren().clear();
        rightPane.getChildren().add(dictionaryPane);
    }

    private EventHandler<ActionEvent> profileButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetButton(event);
                changeToProfilePane();
            }
        };
    }


    private EventHandler<ActionEvent> topicButtonClickedEventHanlder() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetButton(event);
                changeToListTopicPane();
            }
        };
    }

    private EventHandler<ActionEvent> translateButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetButton(event);
                changeToTranslatePane();
            }
        };
    }

    private EventHandler<ActionEvent> logoutButtonClickedEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                resetButton(actionEvent);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Logout Confirmation");
                alert.setHeaderText("Are you sure you want to log out?");
                alert.setContentText("Click OK to log out, or Cancel to cancel.");

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
        mainController.getTopicScreenController().refreshCardInTopic(topic);
        rightPane.getChildren().clear();
        rightPane.getChildren().add(cardTopicPane);
    }

    public void changeToTranslatePane() {
        rightPane.getChildren().clear();
        rightPane.getChildren().add(translatePane);
    }
    public void changeToProfilePane(){
        mainController.getProfileScreenController().refreshUserInfo();

        rightPane.getChildren().clear();
        rightPane.getChildren().add(profilePane);
    }
    public void setAnchor(Pane pane){
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
    }

    public void changeToListenGamePane(){
        rightPane.getChildren().clear();
        rightPane.getChildren().add(wordListenGamePane);
    }

    public void changeToWordGamePane(){
        rightPane.getChildren().clear();
        rightPane.getChildren().add(wordCombinGamePane);
    }

    public void changeToLoginScreen()
    {
        try
        {
            HttpClient httpClient = HttpClient.newHttpClient();
            String requestBody = String.format (
                    "{\"token\":\"%s\"}",
                    TokenManager.getInstance().getToken()
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/auth/logout"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200)
            {
                Stage currentStage = (Stage)logoutButton.getScene().getWindow();
                Stage loginStage = new Stage();
                loginScreen.refreshData();
                Parent loginRoot = loginScreen.getRoot();
                Scene scene;
                if(loginRoot.getScene() == null){
                    scene = new Scene(loginRoot);
                }else{
                    scene = loginRoot.getScene();
                }
                loginStage.setScene(scene);
                loginStage.setResizable(false);
                TokenManager.getInstance().clearToken();
                loginStage.show();
                currentStage.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void setLoginScreen(LoginScreen loginScreen) {
        this.loginScreen = loginScreen;
    }

    public void refresh() {
        mainController.refresh();
    }
    public void resetButton(Event event){
        if(currentButton != null) currentButton.setStyle("");
        Node node = (Node) event.getSource();
        currentButton = (Button) node;
        node.setStyle("    -fx-border-width:  0 0 0 4;\n" +
                "    -fx-border-color: #9659D3;\n" +
                "    -fx-border-insets: 0 0 0 -10;");
    }
}
