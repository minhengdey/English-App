package com.example.apienglishapp.controller;

import com.example.apienglishapp.entity.EnglishToVietnameseEntity;
import com.example.apienglishapp.service.EnglishToVietnameseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/english_to_vietnamese")
public class EnglishToVietnameseController {

    @Autowired
    private EnglishToVietnameseService englishToVietnameseService;

    @GetMapping
    public List<EnglishToVietnameseEntity> getEnglishToVietnamese () {
        return englishToVietnameseService.getEnglishToVietnamese();
    }
    @GetMapping("/byId/{id}")
    public EnglishToVietnameseEntity getEnglishToVietnameseByWord (@PathVariable("id") Integer id) {
        return englishToVietnameseService.getEnglishToVietnameseByWord(id);
    }
    @GetMapping("/byWord/{word}")
    public EnglishToVietnameseEntity getEnglishToVietnameseByWord (@PathVariable("word") String word) {
        return englishToVietnameseService.getEnglishToVietnameseByWord(word);
    }
}