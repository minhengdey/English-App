package com.example.demo.repository;

import com.example.demo.entity.EnglishToVietnamese;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnglishToVietnameseRepository extends JpaRepository <EnglishToVietnamese, String> {
    Optional<Object> findByWord(String word);
}
