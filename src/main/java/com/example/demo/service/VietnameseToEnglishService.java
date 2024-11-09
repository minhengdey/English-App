package com.example.demo.service;

import com.example.demo.entity.VietnameseToEnglish;
import com.example.demo.repository.VietnameseToEnglishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VietnameseToEnglishService {
    @Autowired
    private VietnameseToEnglishRepository vietnameseToEnglishRepository;

//    public VietnameseToEnglish createVietnameseToEnglish(VietnameseToEnglishCreationRequest request) {
//        VietnameseToEnglish VietnameseToEnglish = new VietnameseToEnglish();
//        VietnameseToEnglish.setId(request.getId());
//        VietnameseToEnglish.setWord(request.getWord());
//        VietnameseToEnglish.setHtml(request.getHtml());
//        VietnameseToEnglish.setDescription(request.getDescription());
//        VietnameseToEnglish.setPronounce(request.getPronounce());
//
//        return vietnameseToEnglishRepository.save(VietnameseToEnglish);
//    }

    public List<VietnameseToEnglish> getVietnameseToEnglish() {
        return vietnameseToEnglishRepository.findAll();
    }
    public VietnameseToEnglish getVietnameseToEnglishByWord(Integer id) {
        return vietnameseToEnglishRepository.findById(String.valueOf(id)).orElseThrow(() -> new RuntimeException("Tu nay khong co trong tu dien"));
    }
    public VietnameseToEnglish getVietnameseToEnglishByWord(String word) {
        return (VietnameseToEnglish) vietnameseToEnglishRepository.findByWord(word).orElseThrow(() -> new RuntimeException("Tu nay khong co trong tu dien"));
    }

}
