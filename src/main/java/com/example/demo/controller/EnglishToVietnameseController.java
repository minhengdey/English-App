package com.example.demo.controller;


import com.example.demo.entity.EnglishToVietnamese;
import com.example.demo.service.EnglishToVietnameseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/english_to_vietnamese")
public class EnglishToVietnameseController {
    @Autowired
    private EnglishToVietnameseService englishToVietnameseService;

//    @PostMapping
//    public EnglishToVietnamese createEnglishToVietnamese (@RequestBody EnglishToVietnameseCreationRequest request) {
//        return englishToVietnameseService.createEnglishToVietnamese(request);
//    }
    @GetMapping
    List<EnglishToVietnamese> getEnglishToVietnamese () {
        return englishToVietnameseService.getEnglishToVietnamese();
    }
    @GetMapping("/{id}")
    EnglishToVietnamese getEnglishToVietnameseByWord (@PathVariable("id") Integer id) {
        return englishToVietnameseService.getEnglishToVietnameseByWord(id);
    }
    @GetMapping("/{word}")
    EnglishToVietnamese getEnglishToVietnameseByWord (@PathVariable("word") String word) {
        return englishToVietnameseService.getEnglishToVietnameseByWord(word);
    }
}
//
//{
//        "id" : 1,
//        "word": "a",
//        "html" : "test",
//        "description" : "demo",
//        "pronounce" : "ae"
//        }