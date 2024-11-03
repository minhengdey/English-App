package com.example.apienglishapp.service;

import com.example.apienglishapp.entity.NewWordEntity;
import com.example.apienglishapp.repository.NewWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class NewWordService {

    @Autowired
    private NewWordRepository newWordRepository;

    public NewWordEntity findById (Long id) {
        return newWordRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }

    public NewWordEntity save (NewWordEntity newWord) {
        if (newWord.getId() != null) {
            NewWordEntity newWordEntity = findById (newWord.getId());
            newWordEntity.setEnglish(newWord.getEnglish());
            newWordEntity.setVietnamese(newWord.getVietnamese());
            newWordEntity.setTopic(newWord.getTopic());
        }
        return newWordRepository.save(newWord);
    }

    public void deleteById (Long id) {
        newWordRepository.deleteById(id);
    }

    public List<NewWordEntity> getAll () {
        return newWordRepository.findAll();
    }

    public List<NewWordEntity> getAllByTopic (String topic) {
        List<NewWordEntity> list = new LinkedList<>();
        List<NewWordEntity> listAll = newWordRepository.findAll();
        for (NewWordEntity newWord : listAll) {
            if (newWord.getTopic().equals(topic)) {
                list.add(newWord);
            }
        }
        return list;
    }
}
