package com.example.apienglishapp.controller;

import com.example.apienglishapp.entity.EnglishToVietnameseEntity;
import com.example.apienglishapp.service.EnglishToVietnameseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
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
        try {
            String decodedWord = URLDecoder.decode(word, StandardCharsets.UTF_8);
            return englishToVietnameseService.getEnglishToVietnameseByWord(decodedWord);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
