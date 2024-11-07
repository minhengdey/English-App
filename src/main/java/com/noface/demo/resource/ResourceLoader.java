package com.noface.demo.resource;

import com.noface.demo.card.Card;
import com.noface.demo.topic.Topic;

import java.util.ArrayList;
import java.util.List;

public class ResourceLoader {
    private static ResourceLoader resourceLoader;
    private List<Card> cards;
    private List<Topic> topics;
    public static ResourceLoader getInstance(){
        if(resourceLoader == null){
            resourceLoader = new ResourceLoader();
        }
        return resourceLoader;
    }
    private ResourceLoader(){
        cards = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Card card = new Card(String.valueOf(i + 1), "Front side " + i, "Back side " + i);
            cards.add(card);
        }

        topics = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Topic topic = new Topic(String.format("Topic %d", i + 1), "");
            topics.add(topic);
        }
    }
    public List<Card> getCardsSampleData(){

        return cards;
    }
    public List<Topic> getTopicsSampleData(){

        return topics;
    }

}
