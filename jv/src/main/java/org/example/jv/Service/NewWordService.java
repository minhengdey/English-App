package org.example.jv.Service;

import org.example.jv.Entity.NewWordEntity;
import org.example.jv.Repository.NewWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        }
        return newWordRepository.save(newWord);
    }

    public void deleteById (Long id) {
        newWordRepository.deleteById(id);
    }

    public List<NewWordEntity> getAll () {
        return newWordRepository.findAll();
    }
}
