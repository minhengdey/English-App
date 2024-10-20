
package com.noface.demo.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.glass.ui.Cursor;
import com.sun.jdi.event.Event;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.util.ArrayList;
public class CardView {
    // Sample html content
    private String htmlContent = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Sample HTML</title>
            <style>
                body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; }
                h1 { color: #336699; }
                p { font-size: 18px; color: #333333; }
            </style>
        </head>
        <body>
            <h1>Welcome to JavaFX WebView</h1>
            <p>This is HTML content loaded from a string.</p>
        </body>
        </html>
        """;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        defaultTest();
        initUI();
    }
    private void initUI(){

        // Init Button bar
        showAnswerButton = new Button("Show");
        showAnswerButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                setOnAnswerShow();
            }
            
        });
        
        doneButton = new ArrayList<>();
        String[] doneButtonLabels = {"Easy - 5 days", "Medium - 3 days", "Hard - 1 days", "Super Hard - 2 hour"};
        for(String label : doneButtonLabels){
            Button button = new Button(label);
            doneButton.add(button);
            button.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent event) {
                    setOnAnswerHided();
                }
                
            });
        }        

        // Default state
        setOnAnswerHided();
        
    }

    @FXML
    private HBox bottomButtonBar;
    @FXML
    private WebView frontView;
    @FXML
    private WebView backView;

    private Button showAnswerButton;
    private ArrayList<Button> doneButton;
    

    private void defaultTest() {
        frontView.getEngine().loadContent(htmlContent, "text/html");
    }

    private void setOnAnswerHided() {
        bottomButtonBar.getChildren().setAll(showAnswerButton);
        backView.getEngine().loadContent("");
    }

    private void setOnAnswerShow(){
        bottomButtonBar.getChildren().setAll(doneButton);
        for(Button button : doneButton){
            bottomButtonBar.setMargin(button, new Insets(0, 10, 0, 10));
        }
        backView.getEngine().loadContent(htmlContent, "text/html");

    }

}
