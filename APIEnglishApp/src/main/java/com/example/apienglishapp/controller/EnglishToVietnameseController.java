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
    @GetMapping("/byWord/{word}")
    public List<EnglishToVietnameseEntity> getEnglishToVietnameseByWord (@PathVariable("word") String word) {
        return englishToVietnameseService.getEnglishToVietnameseByWord(word);
    }
}