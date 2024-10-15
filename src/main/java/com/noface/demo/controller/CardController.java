package com.noface.demo.controller;

import com.noface.demo.model.Card;
import com.noface.demo.resource.ResourceLoader;
import com.noface.demo.view.BrowseUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardController {
    private BrowseUI browseUI;

    public CardController(BrowseUI browseUI) {
        this.browseUI = browseUI;
    }
    public void updateViewCard(){
        List<Card> cards = ResourceLoader.getInstance().getCards();
        browseUI.updateCards(cards);
    }

}
