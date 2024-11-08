package com.example.apienglishapp.controller;

import com.example.apienglishapp.dto.request.NewWordRequest;
import com.example.apienglishapp.dto.response.NewWordResponse;
import com.example.apienglishapp.entity.NewWordEntity;
import com.example.apienglishapp.service.NewWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import jakarta.validation.Valid;

import java.util.List;

@RestController
public class NewWordController {

    @Autowired
    private NewWordService newWordService;

    @GetMapping (value = "/new_word/{id}")
    public NewWordResponse getNewWordById (@PathVariable("id") Long id) {
        return newWordService.findById(id);
    }

    @GetMapping (value = "/new_word/topic/{topic}")
    public List<NewWordResponse> getAllByTopic (@PathVariable("topic") String topic) {
        return newWordService.getAllByTopic(topic);
    }

    @PostMapping (value = "/new_word")
    public NewWordResponse create (@Valid @RequestBody NewWordRequest newWord) {
        return newWordService.create(newWord);
    }

    @PutMapping (value = "new_word/{id}")
    public NewWordResponse update (@RequestBody NewWordRequest newWord, @PathVariable("id") Long id) {
        return newWordService.update(id, newWord);
    }

    @DeleteMapping (value = "new_word/{id}")
    public void delete (@PathVariable("id") Long id) {
        newWordService.deleteById(id);
    }

    @GetMapping (value = "/new_word")
    public List<NewWordResponse> getAll () {
        return newWordService.getAll();
    }
}