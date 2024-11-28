package com.noface.englishapp.controller;

import com.noface.englishapp.model.Card;
import com.noface.englishapp.view.MainScreen;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

public class MainController {
    private ListProperty<String> topics = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<Card> cards = new SimpleListProperty<>(FXCollections.observableArrayList());
    private MainScreen mainScreen;
    private TopicScreenController topicScreenController;
    private CardLearningController cardLearningController;
    private TranslateScreenController translateScreenController;
    private ProfileScreenController profileScreenController;
    private DictionaryScreenController dictionaryScreenController;
    private WordCombineGameController wordCombineGameController;
    private GameScreenController gameScreenController;
    private WordListenGameController wordListenGameController;
    public MainController() throws IOException {
        topicScreenController = new TopicScreenController();
        cardLearningController = new CardLearningController();
        translateScreenController = new TranslateScreenController();
        profileScreenController = new ProfileScreenController();
        dictionaryScreenController = new DictionaryScreenController();
        wordCombineGameController = new WordCombineGameController();
        wordListenGameController = new WordListenGameController();
        gameScreenController = new GameScreenController();

        mainScreen =  new MainScreen(this,
                topicScreenController.setMainScreen().getRoot(),
                topicScreenController.getScreen().getRoot(),
                cardLearningController.getScreen().getRoot(),
                translateScreenController.getScreen().getRoot(),
                profileScreenController.getScreen().getRoot(),
                dictionaryScreenController.getScreen().getRoot(),
                wordCombineGameController.getScreen().getRoot(),
                wordListenGameController.getScreen().getRoot(),
                gameScreenController.getScreen().getRoot()
        );
        setMainScreenForSubScreen(mainScreen);
        mainScreen.changeToListTopicPane();
    }
    public void refresh(){
        wordCombineGameController.getScreen().refresh();
        wordListenGameController.getScreen().refresh();
        translateScreenController.getScreen().refresh();
        dictionaryScreenController.getScreen().refresh();
    }

    private void setMainScreenForSubScreen(MainScreen mainScreen) {
        topicScreenController.setMainScreen().setMainScreen(mainScreen);
        topicScreenController.getScreen().setMainScreen(mainScreen);
        cardLearningController.getScreen().setMainScreen(mainScreen);
        translateScreenController.getScreen().setMainScreen(mainScreen);
        profileScreenController.getScreen().setMainScreen(mainScreen);
        dictionaryScreenController.getScreen().setMainScreen(mainScreen);
        gameScreenController.getScreen().setMainScreen(mainScreen);
        wordCombineGameController.getScreen().setMainScreen(mainScreen);
        wordListenGameController.getScreen().setMainScreen(mainScreen);
    }

    public DictionaryScreenController getDictionaryScreenController() {
        return dictionaryScreenController;
    }


    public TopicScreenController getTopicScreenController() {
        return topicScreenController;
    }

    public CardLearningController getCardLearningController() {
        return cardLearningController;
    }

    public TranslateScreenController getTranslateScreenController() {
        return translateScreenController;
    }

    public ProfileScreenController getProfileScreenController() {
        return profileScreenController;
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
