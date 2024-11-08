package com.example.apienglishapp.service;

import com.example.apienglishapp.dto.request.NewWordRequest;
import com.example.apienglishapp.dto.response.NewWordResponse;
import com.example.apienglishapp.entity.NewWordEntity;
import com.example.apienglishapp.exception.AppException;
import com.example.apienglishapp.exception.ErrorCode;
import com.example.apienglishapp.mapper.NewWordMapper;
import com.example.apienglishapp.repository.NewWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class NewWordService {

    @Autowired
    private NewWordRepository newWordRepository;

    @Autowired
    private NewWordMapper newWordMapper;

    public NewWordResponse findById (Long id) {
        return newWordMapper.toNewWordResponse(newWordRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NEW_WORD_NOT_FOUND)));
    }

    public NewWordResponse create (NewWordRequest newWord) {
        if (newWordRepository.existsByEnglishAndVietnamese(newWord.getVietnamese(), newWord.getEnglish())) {
            throw new AppException(ErrorCode.NEW_WORD_EXISTED);
        }
        NewWordEntity newWordEntity = newWordMapper.toNewWordEntity(newWord);
        return newWordMapper.toNewWordResponse(newWordRepository.save(newWordEntity));
    }

    public NewWordResponse update (Long id, NewWordRequest newWord) {
        NewWordEntity newWordEntity = newWordRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NEW_WORD_NOT_FOUND));
        newWordMapper.updateNewWord(newWordEntity, newWord);
        return newWordMapper.toNewWordResponse(newWordRepository.save(newWordEntity));
    }

    public void deleteById (Long id) {
        newWordRepository.deleteById(id);
    }

    public List<NewWordResponse> getAll () {
        return newWordRepository.findAll().stream().map(newWordMapper::toNewWordResponse).toList();
    }

    public List<NewWordResponse> getAllByTopic (String topic) {
        List<NewWordEntity> list = new LinkedList<>();
        List<NewWordEntity> listAll = newWordRepository.findAll();
        for (NewWordEntity newWord : listAll) {
            if (newWord.getTopic().equals(topic)) {
                list.add(newWord);
            }
        }
        return list.stream().map(newWordMapper::toNewWordResponse).toList();
    }
}
