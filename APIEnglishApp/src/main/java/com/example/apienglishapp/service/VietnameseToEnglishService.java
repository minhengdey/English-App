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
    public VietnameseToEnglishEntity getVietnameseToEnglishByWord(Integer id) {
        return vietnameseToEnglishRepository.findById(String.valueOf(id)).orElseThrow(() -> new RuntimeException("Tu nay khong co trong tu dien"));
    }
    public VietnameseToEnglishEntity getVietnameseToEnglishByWord(String word) {
        return vietnameseToEnglishRepository.findByWord(word).orElseThrow(() -> new RuntimeException("Tu nay khong co trong tu dien"));
    }

}
