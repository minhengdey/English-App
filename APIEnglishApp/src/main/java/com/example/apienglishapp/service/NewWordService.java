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

import java.util.*;
import java.util.stream.Collectors;

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

    public Set<NewWordResponse> getAllNewWordByUserId (Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Set<NewWordEntity> set = user.getNewWords();
        return set.stream().map(newWordMapper::toNewWordResponse).collect(Collectors.toSet());
    }

    public List<NewWordResponse> getAllByTopicAndUserId (String topic, Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Set<NewWordEntity> set = user.getNewWords();
        List<NewWordEntity> list = new ArrayList<>();
        for (NewWordEntity newWord : set) {
            if (newWord.getTopic().equals(topic)) {
                list.add(newWord);
            }
        }
        return list.stream().map(newWordMapper::toNewWordResponse).toList();
    }

    public Set<String> getAllTopic (Long userId) {
        Set<String> setTopic = new TreeSet<>();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Set<NewWordEntity> set = user.getNewWords();
        for (NewWordEntity newWord : set) {
            setTopic.add(newWord.getTopic());
        }
        return setTopic;
    }
}