package com.noface.demo.controller;

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
    private TranslateScreenController translateScreenController;
    private ProfileScreenController profileScreenController;
    public MainController() throws IOException {
        getData();
        topicScreenController = new TopicScreenController();
        cardLearningController = new CardLearningController();
        translateScreenController = new TranslateScreenController();
        profileScreenController = new ProfileScreenController();
        mainScreen =  new MainScreen(this, topicScreenController.getTopicScreen().getRoot(),
                topicScreenController.getScreen().getRoot(),
                cardLearningController.getScreen().getRoot(),
                translateScreenController.getScreen().getRoot(),
                profileScreenController.getScreen().getRoot());
        setMainScreenForSubScreen(mainScreen);
        mainScreen.changeToProfilePane();
    }

    private void setMainScreenForSubScreen(MainScreen mainScreen) {
        topicScreenController.getTopicScreen().setMainScreen(mainScreen);
        topicScreenController.getScreen().setMainScreen(mainScreen);
        cardLearningController.getScreen().setMainScreen(mainScreen);
        translateScreenController.getScreen().setMainScreen(mainScreen);
        profileScreenController.getScreen().setMainScreen(mainScreen);
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
