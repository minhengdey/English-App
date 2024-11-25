package com.example.apienglishapp.service;

import com.example.apienglishapp.entity.VietnameseToEnglishEntity;
import com.example.apienglishapp.repository.VietnameseToEnglishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VietnameseToEnglishService {
    @Autowired
    private VietnameseToEnglishRepository vietnameseToEnglishRepository;

    public List<VietnameseToEnglishEntity> getVietnameseToEnglish() {
        return vietnameseToEnglishRepository.findAll();
    }
    
    public List<VietnameseToEnglishEntity> getVietnameseToEnglishByWord(String word) {
        List<VietnameseToEnglishEntity> allWords = vietnameseToEnglishRepository.findAllWords();
        List<VietnameseToEnglishEntity> results = allWords.stream()
                .filter(entity -> URLEncoder.encode(entity.getWord(), StandardCharsets.UTF_8).equals(word))
                .collect(Collectors.toList());
        if (results.isEmpty()) {
            throw new RuntimeException("Từ này không có trong từ điển");
        }
        return results;
    }

}
