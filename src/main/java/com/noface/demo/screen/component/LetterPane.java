package com.noface.demo.screen.component;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class LetterPane extends StackPane{
    private Text text;
    public LetterPane(String letter) {
        text = new Text(letter);
        this.getChildren().add(text);
    }
    public String getLetter(){
        return text.getText();
    }
}
