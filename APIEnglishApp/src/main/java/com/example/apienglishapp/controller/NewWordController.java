package com.example.apienglishapp.controller;

import com.example.apienglishapp.dto.request.NewWordRequest;
import com.example.apienglishapp.dto.response.NewWordResponse;
import com.example.apienglishapp.exception.AppException;
import com.example.apienglishapp.enums.ErrorCode;
import com.example.apienglishapp.service.NewWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import jakarta.validation.Valid;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@RestController
public class NewWordController {

    @Autowired
    private NewWordService newWordService;

    @GetMapping (value = "/new_word/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public NewWordResponse getNewWordByUserIdAndId (@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();
            return newWordService.findByUserIdAndId(Long.parseLong(jwt.getSubject()), id);
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    @GetMapping (value = "/new_word/topic/{topic}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<NewWordResponse> getAllByTopicAndUserId (@PathVariable("topic") String topic) {
        System.out.println(topic + " " + URLDecoder.decode(topic, StandardCharsets.UTF_8));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();
            return newWordService.getAllByTopicAndUserId(URLDecoder.decode(topic, StandardCharsets.UTF_8), Long.parseLong(jwt.getSubject()));
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    @PostMapping (value = "/new_word/topic/{oldTopic}/{newTopic}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<NewWordResponse> updateTopic (@PathVariable("oldTopic") String oldTopic, @PathVariable("newTopic") String newTopic) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();
            return newWordService.updateTopic(URLDecoder.decode(oldTopic, StandardCharsets.UTF_8),
                    URLDecoder.decode(newTopic, StandardCharsets.UTF_8),
                    Long.parseLong(jwt.getSubject()));
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    @DeleteMapping (value = "/new_word/topic/{topic}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void deleteByTopic (@PathVariable("topic") String topic) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();
            newWordService.deleteByTopic(URLDecoder.decode(topic, StandardCharsets.UTF_8), Long.parseLong(jwt.getSubject()));
        } else {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @PostMapping (value = "/new_word")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public NewWordResponse create (@Valid @RequestBody NewWordRequest newWord) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();
            return newWordService.create(newWord, Long.parseLong(jwt.getSubject()));
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    @PutMapping (value = "new_word/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public NewWordResponse update (@RequestBody NewWordRequest newWord, @PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();
            return newWordService.update(id, newWord, Long.parseLong(jwt.getSubject()));
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    @DeleteMapping (value = "new_word/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void delete (@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();
            newWordService.deleteByUserIdAndId(Long.parseLong(jwt.getSubject()), id);
        } else {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @GetMapping (value = "/new_word")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Set<NewWordResponse> getAllNewWordByUserId () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();
            return newWordService.getAllNewWordByUserId(Long.parseLong(jwt.getSubject()));
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    @GetMapping (value = "/topic")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Set<String> getAllTopic () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();
            return newWordService.getAllTopic(Long.parseLong(jwt.getSubject()));
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
}