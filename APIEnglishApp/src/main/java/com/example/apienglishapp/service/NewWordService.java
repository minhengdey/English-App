package com.example.apienglishapp.service;

import com.example.apienglishapp.dto.request.NewWordRequest;
import com.example.apienglishapp.dto.response.NewWordResponse;
import com.example.apienglishapp.entity.NewWordEntity;
import com.example.apienglishapp.entity.UserEntity;
import com.example.apienglishapp.exception.AppException;
import com.example.apienglishapp.exception.ErrorCode;
import com.example.apienglishapp.mapper.NewWordMapper;
import com.example.apienglishapp.repository.NewWordRepository;
import com.example.apienglishapp.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    public NewWordResponse findById (Long id) {
        return newWordMapper.toNewWordResponse(newWordRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NEW_WORD_NOT_FOUND)));
    }

    public NewWordResponse create (NewWordRequest newWordRequest, Long userId) {
        if (newWordRepository.existsByEnglishAndVietnamese(newWordRequest.getVietnamese(), newWordRequest.getEnglish())) {
            throw new AppException(ErrorCode.NEW_WORD_EXISTED);
        }
        return newWordMapper.toNewWordResponse(userRepository.findById(userId)
                .map(userEntity -> {
                    newWordRequest.setUser(userEntity);
            return newWordRepository.save(newWordMapper.toNewWordEntity(newWordRequest));
        }).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public NewWordResponse update (Long id, NewWordRequest newWord) {
        NewWordEntity newWordEntity = newWordRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NEW_WORD_NOT_FOUND));
        newWordMapper.updateNewWord(newWordEntity, newWord);
        return newWordMapper.toNewWordResponse(newWordRepository.save(newWordEntity));
    }

    public void deleteById (Long id) {
        newWordRepository.deleteById(id);
    }

    public List<NewWordResponse> getAll (Long userId) {
        List<NewWordEntity> list = new LinkedList<>();
        List<NewWordEntity> listAll = newWordRepository.findAll();
        for (NewWordEntity newWord : listAll) {
            if (newWord.getUser() != null && newWord.getUser().getId().equals(userId)) {
                list.add(newWord);
            }
        }
        return list.stream().map(newWordMapper::toNewWordResponse).toList();
    }

    public List<NewWordResponse> getAllByTopic (String topic, Long userId) {
        List<NewWordEntity> list = new LinkedList<>();
        List<NewWordEntity> listAll = newWordRepository.findAll();
        for (NewWordEntity newWord : listAll) {
            if (newWord.getTopic().equals(topic) && newWord.getUser() != null && newWord.getUser().getId().equals(userId)) {
                list.add(newWord);
            }
        }
        return list.stream().map(newWordMapper::toNewWordResponse).toList();
    }
}
