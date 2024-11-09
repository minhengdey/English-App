package com.example.demo.controller;

import com.example.demo.entity.VietnameseToEnglish;
import com.example.demo.service.VietnameseToEnglishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("vietnamese_to_english")
public class VietnameseToEnglishController {
    @Autowired
    private VietnameseToEnglishService vietnameseToEnglishService;

//    @PostMapping
//    public VietnameseToEnglish createVietnameseToEnglish(@RequestBody VietnameseToEnglishCreationRequest request) {
//        return vietnameseToEnglishService.createVietnameseToEnglish(request);
//    }
    @GetMapping
    public List<VietnameseToEnglish> getVietnameseToEnglish () {
        return vietnameseToEnglishService.getVietnameseToEnglish();
    }
    @GetMapping("/{id}")
    public VietnameseToEnglish getVietnameseToEnglishByWord (@PathVariable("id") Integer id) {
        return vietnameseToEnglishService.getVietnameseToEnglishByWord(id);
    }
    @GetMapping("/{word}")
    public VietnameseToEnglish getVietnameseToEnglishByWord (@PathVariable("word") String word) {
        return vietnameseToEnglishService.getVietnameseToEnglishByWord(word);
    }
}
