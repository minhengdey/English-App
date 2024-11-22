package com.noface.demo.resource;

import com.noface.demo.model.CardCRUD;
import com.noface.demo.model.Card;
import com.noface.demo.model.UserCRUD;
import com.noface.demo.screen.CardTopicScreen;
import com.noface.demo.screen.ListTopicScreen;
import javafx.fxml.FXMLLoader;

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
