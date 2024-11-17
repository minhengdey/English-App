package com.example.apienglishapp.repository;

import com.example.apienglishapp.entity.VietnameseToEnglishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VietnameseToEnglishRepository extends JpaRepository <VietnameseToEnglishEntity, String> {
    @Query(value = "SELECT * FROM va WHERE BINARY word = :word", nativeQuery = true)
    List<VietnameseToEnglishEntity> findByWord(String word);
}