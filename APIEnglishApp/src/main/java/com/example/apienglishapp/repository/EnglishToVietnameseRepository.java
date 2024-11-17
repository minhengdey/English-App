package com.example.apienglishapp.repository;

import com.example.apienglishapp.entity.EnglishToVietnameseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnglishToVietnameseRepository extends JpaRepository <EnglishToVietnameseEntity, String> {
    @Query("SELECT e FROM EnglishToVietnameseEntity e WHERE e.word = :word")
    List<EnglishToVietnameseEntity> findByWord(String word);
}