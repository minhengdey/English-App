package com.example.demo.service;

import com.example.demo.entity.EnglishToVietnamese;
import com.example.demo.repository.EnglishToVietnameseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnglishToVietnameseService {
    @Autowired
    private EnglishToVietnameseRepository englishToVietnameseRepository;

//    public EnglishToVietnamese createEnglishToVietnamese(EnglishToVietnameseCreationRequest request) {
//        EnglishToVietnamese englishToVietnamese = new EnglishToVietnamese();
//        englishToVietnamese.setId(request.getId());
//        englishToVietnamese.setWord(request.getWord());
//        englishToVietnamese.setHtml(request.getHtml());
//        englishToVietnamese.setDescription(request.getDescription());
//        englishToVietnamese.setPronounce(request.getPronounce());
//
//        return englishToVietnameseRepository.save(englishToVietnamese);
//    }
    public List<EnglishToVietnamese> getEnglishToVietnamese () {
        return englishToVietnameseRepository.findAll();
    }
    public EnglishToVietnamese getEnglishToVietnameseByWord(Integer id) {
        return englishToVietnameseRepository.findById(String.valueOf(id)).orElseThrow(() -> new RuntimeException("Tu nay khong co trong tu dien"));
    }
    public EnglishToVietnamese getEnglishToVietnameseByWord(String word) {
        return (EnglishToVietnamese) englishToVietnameseRepository.findByWord(word)
                .orElseThrow(() -> new RuntimeException("Từ này không có trong từ điển"));
    }
}
