package com.noface.demo.resource;

import com.noface.demo.model.CardCRUD;
import com.noface.demo.model.Card;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResourceLoader {
    private static ResourceLoader resourceLoader;
    private List<Card> cards;
    private CardCRUD cardCRUD = new CardCRUD();

    public static ResourceLoader getInstance(){
        if(resourceLoader == null){
            resourceLoader = new ResourceLoader();
        }
        return resourceLoader;
    }
    private ResourceLoader(){

    }
    public CardCRUD getCardCRUD(){
        return cardCRUD;
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
