package com.example.apienglishapp.controller;

import com.example.apienglishapp.entity.VietnameseToEnglishEntity;
import com.example.apienglishapp.service.VietnameseToEnglishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/vietnamese_to_english")
public class VietnameseToEnglishController {
    @Autowired
    private VietnameseToEnglishService vietnameseToEnglishService;

    @GetMapping
    public List<VietnameseToEnglishEntity> getVietnameseToEnglish () {
        return vietnameseToEnglishService.getVietnameseToEnglish();
    }

    @GetMapping("/byWord/{word}")
    public List<VietnameseToEnglishEntity> getVietnameseToEnglishByWord(@PathVariable("word") String word) {
        word = word.replace("+", " ");
        try {
            String encodedWord = URLEncoder.encode(word, StandardCharsets.UTF_8);
            return vietnameseToEnglishService.getVietnameseToEnglishByWord(encodedWord);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}