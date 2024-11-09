package com.example.apienglishapp.repository;

import com.example.apienglishapp.entity.EnglishToVietnameseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnglishToVietnameseRepository extends JpaRepository <EnglishToVietnameseEntity, String> {
    Optional<EnglishToVietnameseEntity> findByWord(String word);
}
