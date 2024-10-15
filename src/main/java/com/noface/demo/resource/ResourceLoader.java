package com.noface.demo.resource;

import com.noface.demo.model.Card;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;

public class ResourceLoader {
    private final List<Card> cards;
    private static ResourceLoader resourceLoader;
    public static ResourceLoader getInstance(){
        if(resourceLoader == null){
            resourceLoader = new ResourceLoader();
        }
        return resourceLoader;
    }
    private ResourceLoader(){
        cards = new ArrayList<>();
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add("hehe");
        tmp.add("hoho");
        cards.add(new Card("1", tmp, LocalDate.now()));
        cards.add(new Card("2", (ArrayList<String>) tmp.clone(), LocalDate.now()));
    }
    public List<Card> getCards(){
        return cards;
    }

}
