package com.example.apienglishapp.controller;

import com.example.apienglishapp.dto.request.NewWordRequest;
import com.example.apienglishapp.dto.response.NewWordResponse;
import com.example.apienglishapp.service.NewWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping (value = "/new_word/{id}/{userId}/user")
    @PreAuthorize("hasRole('ADMIN') or (#userId.toString() == authentication?.token?.claims['sub'])")
    public NewWordResponse getNewWordByUserIdAndId (@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return newWordService.findByUserIdAndId(userId, id);
    }

    @GetMapping (value = "/new_word/topic/{topic}/{userId}/user")
    @PreAuthorize("hasRole('ADMIN') or (#userId.toString() == authentication?.token?.claims['sub'])")
    public List<NewWordResponse> getAllByTopicAndUserId (@PathVariable("topic") String topic, @PathVariable("userId") Long userId) {
        return newWordService.getAllByTopicAndUserId(topic, userId);
    }

    @PostMapping (value = "/new_word/{userId}/user")
    @PreAuthorize("hasRole('ADMIN') or (#userId.toString() == authentication?.token?.claims['sub'])")
    public NewWordResponse create (@Valid @RequestBody NewWordRequest newWord, @PathVariable("userId") Long userId) {
        return newWordService.create(newWord, userId);
    }

    @PutMapping (value = "new_word/{id}/{userId}/user")
    @PreAuthorize("hasRole('ADMIN') or (#userId.toString() == authentication?.token?.claims['sub'])")
    public NewWordResponse update (@RequestBody NewWordRequest newWord, @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return newWordService.update(id, newWord, userId);
    }

    @DeleteMapping (value = "new_word/{id}/{userId}/user")
    @PreAuthorize("hasRole('ADMIN') or (#userId.toString() == authentication?.token?.claims['sub'])")
    public void delete (@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        newWordService.deleteByUserIdAndId(userId, id);
    }

    @GetMapping (value = "/new_word/{userId}/user")
    @PreAuthorize("hasRole('ADMIN') or (#userId.toString() == authentication?.token?.claims['sub'])")
    public List<NewWordResponse> getAllNewWordByUserId (@PathVariable("userId") Long userId) {
        return newWordService.getAllNewWordByUserId(userId);
    }
}