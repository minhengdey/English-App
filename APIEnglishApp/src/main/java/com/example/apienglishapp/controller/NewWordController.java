package com.example.apienglishapp.controller;

import com.example.apienglishapp.dto.request.NewWordRequest;
import com.example.apienglishapp.dto.response.NewWordResponse;
import com.example.apienglishapp.service.NewWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
    @PostAuthorize("returnObject.user.id.toString() == authentication.token.claims['sub'] or hasRole('ADMIN')")
    public NewWordResponse getNewWordById (@PathVariable("id") Long id) {
        return newWordService.findById(id);
    }

    @GetMapping (value = "/new_word/topic/{topic}/{userId}/user")
    @PostAuthorize("#userId.toString() == authentication.token.claims['sub'] or hasRole('ADMIN')")
    public List<NewWordResponse> getAllByTopic (@PathVariable("topic") String topic, @PathVariable("userId") Long userId) {
        return newWordService.getAllByTopic(topic, userId);
    }

    @PostMapping (value = "/new_word/{userId}/user")
    @PostAuthorize("returnObject.user.id.toString() == authentication.token.claims['sub'] or hasRole('ADMIN')")
    public NewWordResponse create (@Valid @RequestBody NewWordRequest newWord, @PathVariable("userId") Long userId) {
        return newWordService.create(newWord, userId);
    }

    @PutMapping (value = "new_word/{id}")
    @PostAuthorize("returnObject.user.id.toString() == authentication.token.claims['sub'] or hasRole('ADMIN')")
    public NewWordResponse update (@RequestBody NewWordRequest newWord, @PathVariable("id") Long id) {
        return newWordService.update(id, newWord);
    }

    @DeleteMapping (value = "new_word/{id}")
    @PostAuthorize("returnObject.user.id.toString() == authentication.token.claims['sub'] or hasRole('ADMIN')")
    public void delete (@PathVariable("id") Long id) {
        newWordService.deleteById(id);
    }

    @GetMapping (value = "/new_word/{userId}/user")
    @PostAuthorize("#userId.toString() == authentication.token.claims['sub'] or hasRole('ADMIN')")
    public List<NewWordResponse> getAll (@PathVariable("userId") Long userId) {
        return newWordService.getAll(userId);
    }
}