package com.noface.demo.topic;

import com.noface.demo.card.Card;
import com.noface.demo.resource.ResourceLoader;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class TopicScreenController {
    private ListProperty<Topic> topics = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<Card> cards = new SimpleListProperty<>(FXCollections.observableArrayList());
    private TopicScreen topicScreen;
    public TopicScreenController(TopicScreen topicScreen) {
        this.topicScreen = topicScreen;

        configure();
        topicScreen.connect(this);
        scrapeData();



    }

    public void configure(){
        topics.addListener(new ListChangeListener<Topic>() {
            @Override
            public void onChanged(Change<? extends Topic> c) {
                List<TreeItem<String>> titles = new ArrayList<>();
                for(Topic topic : topics){
                    TreeItem topicTitleItem = new TreeItem(topic.getTitle());
                    titles.add(topicTitleItem);
                }
                topicScreen.updateTopicTitles(titles);
            }
        });
    }
    public void scrapeData(){
        // Scrape topic data
        topics.addAll(ResourceLoader.getInstance().getTopicsSampleData());
        cards.addAll(ResourceLoader.getInstance().getCardsSampleData());
    }

    public ObservableList<Topic> getTopics() {
        return topics.get();
    }

    public ListProperty<Topic> topicsProperty() {
        return topics;
    }

    public ObservableList<Card> getCards() {
        return cards.get();
    }

    public ListProperty<Card> cardsProperty() {
        return cards;
    }
}
