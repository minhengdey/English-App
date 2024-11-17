package com.example.apienglishapp.service;

import com.example.apienglishapp.entity.VietnameseToEnglishEntity;
import com.example.apienglishapp.repository.VietnameseToEnglishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VietnameseToEnglishService {
    @Autowired
    private VietnameseToEnglishRepository vietnameseToEnglishRepository;

    public List<VietnameseToEnglishEntity> getVietnameseToEnglish() {
        return vietnameseToEnglishRepository.findAll();
    }

    public List<VietnameseToEnglishEntity> getVietnameseToEnglishByWord(String word) {
        List<VietnameseToEnglishEntity> result = vietnameseToEnglishRepository.findByWord(word);
        if (result.isEmpty()) {
            throw new RuntimeException("Từ này không có trong từ điển");
        }
        return result;
    }

}