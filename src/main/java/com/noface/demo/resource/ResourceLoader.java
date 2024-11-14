package com.noface.demo.resource;

import com.noface.demo.model.Card;

import java.util.ArrayList;
import java.util.List;

public class ResourceLoader {
    private static ResourceLoader resourceLoader;
    private List<Card> cards;

    public static ResourceLoader getInstance(){
        if(resourceLoader == null){
            resourceLoader = new ResourceLoader();
        }
        return resourceLoader;
    }
    private ResourceLoader(){
        cards = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            Card card = new Card(String.format("Name %d", i + 1), "Front side " + i, "Back side " + i, "Topic 1");
            cards.add(card);
        }

    }
    public List<String> getTopicTitles(){
        List<String> titles = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            titles.add(String.format("Topic %d", i + 1));
        }
        return titles;
    }
    public List<Card> getCardsSampleData(){

        return cards;
    }


}
