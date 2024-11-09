package com.example.apienglishapp.controller;

import com.example.apienglishapp.entity.VietnameseToEnglishEntity;
import com.example.apienglishapp.service.VietnameseToEnglishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/byId/{id}")
    public VietnameseToEnglishEntity getVietnameseToEnglishByWord (@PathVariable("id") Integer id) {
        return vietnameseToEnglishService.getVietnameseToEnglishByWord(id);
    }
    @GetMapping("/byWord/{word}")
    public VietnameseToEnglishEntity getVietnameseToEnglishByWord (@PathVariable("word") String word) {
        return vietnameseToEnglishService.getVietnameseToEnglishByWord(word);
    }
}
