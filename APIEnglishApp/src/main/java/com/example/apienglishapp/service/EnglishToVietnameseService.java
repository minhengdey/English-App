package com.example.apienglishapp.service;

import com.example.apienglishapp.entity.EnglishToVietnameseEntity;
import com.example.apienglishapp.repository.EnglishToVietnameseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnglishToVietnameseService {
    @Autowired
    private EnglishToVietnameseRepository englishToVietnameseRepository;

    public List<EnglishToVietnameseEntity> getEnglishToVietnamese () {
        return englishToVietnameseRepository.findAll();
    }
    public EnglishToVietnameseEntity getEnglishToVietnameseByWord(Integer id) {
        return englishToVietnameseRepository.findById(String.valueOf(id)).orElseThrow(() -> new RuntimeException("Tu nay khong co trong tu dien"));
    }
    public EnglishToVietnameseEntity getEnglishToVietnameseByWord(String word) {
        return englishToVietnameseRepository.findByWord(word)
                .orElseThrow(() -> new RuntimeException("Từ này không có trong từ điển"));
    }
}