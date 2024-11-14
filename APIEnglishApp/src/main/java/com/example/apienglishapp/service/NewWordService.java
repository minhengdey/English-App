package com.example.apienglishapp.service;

import com.example.apienglishapp.dto.request.NewWordRequest;
import com.example.apienglishapp.dto.response.NewWordResponse;
import com.example.apienglishapp.entity.NewWordEntity;
import com.example.apienglishapp.exception.AppException;
import com.example.apienglishapp.exception.ErrorCode;
import com.example.apienglishapp.mapper.NewWordMapper;
import com.example.apienglishapp.repository.NewWordRepository;
import com.example.apienglishapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public NewWordResponse findByUserIdAndId (Long userId, Long id) {
        return newWordMapper.toNewWordResponse(newWordRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new AppException(ErrorCode.NEW_WORD_NOT_FOUND)));
    }

    public NewWordResponse create (NewWordRequest newWordRequest, Long userId) {
        if (newWordRepository.existsByFrontSideAndBackSide(newWordRequest.getFrontSide(), newWordRequest.getBackSide())) {
            throw new AppException(ErrorCode.NEW_WORD_EXISTED);
        }
        return newWordMapper.toNewWordResponse(userRepository.findById(userId)
                .map(userEntity -> {
                    newWordRequest.setUser(userEntity);
            return newWordRepository.save(newWordMapper.toNewWordEntity(newWordRequest));
        }).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public NewWordResponse update (Long id, NewWordRequest newWord, Long userId) {
        NewWordEntity newWordEntity = newWordRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new AppException(ErrorCode.NEW_WORD_NOT_FOUND));
        newWordMapper.updateNewWord(newWordEntity, newWord);
        return newWordMapper.toNewWordResponse(newWordRepository.save(newWordEntity));
    }

    public void deleteByUserIdAndId (Long userId, Long id) {
        newWordRepository.deleteByUserIdAndId(userId, id);
    }

    public List<NewWordResponse> getAllNewWordByUserId (Long userId) {
        List<NewWordEntity> list = new ArrayList<>();
        List<NewWordEntity> listAll = newWordRepository.findAll();
        for (NewWordEntity newWord : listAll) {
            if (newWord.getUser() != null && newWord.getUser().getId().equals(userId)) {
                list.add(newWord);
            }
        }
        return list.stream().map(newWordMapper::toNewWordResponse).toList();
    }

    public List<NewWordResponse> getAllByTopicAndUserId (String topic, Long userId) {
        List<NewWordEntity> list = new ArrayList<>();
        List<NewWordEntity> listAll = newWordRepository.findAll();
        for (NewWordEntity newWord : listAll) {
            if (newWord.getTopic().equals(topic) && newWord.getUser() != null && newWord.getUser().getId().equals(userId)) {
                list.add(newWord);
            }
        }
        return list.stream().map(newWordMapper::toNewWordResponse).toList();
    }
}
