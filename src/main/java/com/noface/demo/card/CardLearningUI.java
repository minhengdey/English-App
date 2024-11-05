
package com.noface.demo.card;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.ArrayList;


public class CardLearningUI {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

    // Component list.
    @FXML
    private VBox root;
    @FXML
    private HBox bottomButtonBar;
    @FXML
    private WebView frontView;
    @FXML
    private WebView backView;
    @FXML
    private Button cardEditButton;
    public final String[] doneButtonLabels = {"Again - 1 minutes", "Hard - 6 minutes", "Good - 10 minutes", "Easy - 3 days"};
    public final long[] times = {60, 360, 600, 3 * 24 * 60 * 60};

    private Button showAnswerButton = new Button("Show Answer");
    private List<Button> doneButton = new ArrayList<>();


    private StringProperty frontContent = new SimpleStringProperty();
    private StringProperty backContent = new SimpleStringProperty();
    private BooleanProperty backCardShowed = new SimpleBooleanProperty();
    private CardInteractor interactor;


    // Create binding to model
    public void setup(Card card, CardInteractor interactor) {
        System.out.println("In setup card " + card);
        this.interactor = interactor;
        setUpBinding(card);
        initScreenComponent();
        initContent();
    }

    public void setUpBinding(Card card) {
        backCardShowed.bind(card.backCardShowedProperty());
        frontContent.bind(card.frontContentProperty());
        backContent.bind(card.backContentProperty());


        backCardShowed.addListener((observable, oldValue, newValue) -> {
            if (backCardShowed.get()) {
                frontView.getEngine().loadContent(frontContent.get());
                backView.getEngine().loadContent(backContent.get());
            } else {
                frontView.getEngine().loadContent(frontContent.get());
                backView.getEngine().loadContent("");
            }
        });
        frontContent.addListener((observable, oldValue, newValue) -> {
            frontView.getEngine().loadContent(frontContent.get());
        });
        backContent.addListener((observable, oldValue, newValue) -> {
            if(backCardShowed.get()) {
                backView.getEngine().loadContent(backContent.get());
            }else{
                backView.getEngine().loadContent("");
            }
        });


    }

    public void initContent() {
        bottomButtonBar.getChildren().clear();
        bottomButtonBar.getChildren().add(showAnswerButton);
        frontView.getEngine().loadContent(frontContent.get());
        backView.getEngine().loadContent("");
    }

    public void initScreenComponent(){
        // Done button
        doneButton.clear();
        for (int i = 0; i < times.length; i++) {
            String label = doneButtonLabels[i];
            Button button = new Button(label);
            doneButton.add(button);
            int finalI = i;
            button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    interactor.hideBackSide();
                    interactor.addCardTime(times[finalI]);
                    addShowButtonToScene();
                    interactor.setCard(null);
                }

            });
        }
        // Change to card edit screen button
        cardEditButton.setOnAction(event -> {
            try {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.setScene(interactor.changeToEditCardUI(root.getScene()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // Show answer button
        showAnswerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                interactor.showBackSide();
                addDoneButtonToScene();
            }
        });
    }

    public void addDoneButtonToScene() {
        bottomButtonBar.getChildren().clear();
        for(Button button : doneButton) {
            bottomButtonBar.getChildren().add(button);
            bottomButtonBar.setMargin(button, new Insets(0, 10, 0, 10));
        }
    }
    public void addShowButtonToScene() {
        bottomButtonBar.getChildren().clear();
        bottomButtonBar.getChildren().add(showAnswerButton);
    }
    public void clearContentUI(){
        frontView.getEngine().loadContent("");
        backView.getEngine().loadContent("");
        bottomButtonBar.getChildren().clear();
    }

}
