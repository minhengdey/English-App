package com.noface.englishapp.utils;

import com.noface.englishapp.model.Card;

import java.util.List;

public class ResourceLoader {
    private static ResourceLoader resourceLoader;
    private List<Card> cards;
    private CardCRUD cardCRUD = new CardCRUD();
    public UserCRUD userCRUD = new UserCRUD();

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
    public UserCRUD userCRUD(){
        return userCRUD;
    }
}
