package com.noface.demo.Controller;

import com.noface.demo.card.Card;
import com.noface.demo.resource.ResourceLoader;
import com.noface.demo.screen.MainScreen;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private ListProperty<String> topics = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<Card> cards = new SimpleListProperty<>(FXCollections.observableArrayList());
    private MainScreen mainScreen;
    private TopicScreenController topicScreenController;
    private CardLearningController cardLearningController;
    public MainController() throws IOException {
        getData();
        topicScreenController = new TopicScreenController();
        cardLearningController = new CardLearningController();

        mainScreen =  new MainScreen(this, topicScreenController.getTopicScreen().getRoot(),
                topicScreenController.getCardScreen().getRoot(),
                cardLearningController.getCardLearningScreen().getRoot());
        setMainScreenForSubScreen(mainScreen);
    }

    private void setMainScreenForSubScreen(MainScreen mainScreen) {
        topicScreenController.getTopicScreen().setMainScreen(mainScreen);
        topicScreenController.getCardScreen().setMainScreen(mainScreen);
        cardLearningController.getCardLearningScreen().setMainScreen(mainScreen);
    }

    public void getData(){
        List<String> topicTitle = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            topicTitle.add(String.format("Topic %d", i));

        }
        topics.addAll(topicTitle);
        cards.addAll(ResourceLoader.getInstance().getCardsSampleData());
    }
    public MainScreen getMainScreen() {
        return mainScreen;
    }

    public ObservableList<String> getTopics() {
        return topics.get();
    }

    public ListProperty<String> topicsProperty() {
        return topics;
    }

    public ObservableList<Card> getCards() {
        return cards.get();
    }

    public ListProperty<Card> cardsProperty() {
        return cards;
    }
}
