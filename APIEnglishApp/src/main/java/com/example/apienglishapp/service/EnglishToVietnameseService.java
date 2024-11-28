package com.example.apienglishapp.service;

import com.example.apienglishapp.entity.EnglishToVietnameseEntity;
import com.example.apienglishapp.exception.AppException;
import com.example.apienglishapp.enums.ErrorCode;
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

    public List<EnglishToVietnameseEntity> getEnglishToVietnameseByWord(String word) {
        List<EnglishToVietnameseEntity> result = englishToVietnameseRepository.findByWord(word);
        if (result.isEmpty()) {
            throw new AppException(ErrorCode.NEW_WORD_NOT_FOUND);
        }
        return result;
    }
}