package com.example.apienglishapp.repository;

import com.example.apienglishapp.entity.VietnameseToEnglishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VietnameseToEnglishRepository extends JpaRepository <VietnameseToEnglishEntity, String> {
    Optional<VietnameseToEnglishEntity> findByWord(String word);

}
