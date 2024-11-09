package com.example.demo.repository;

import com.example.demo.entity.VietnameseToEnglish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VietnameseToEnglishRepository extends JpaRepository <VietnameseToEnglish, String> {
    Optional<Object> findByWord(String word);

}
