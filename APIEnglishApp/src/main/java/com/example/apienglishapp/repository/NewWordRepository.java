package com.example.apienglishapp.repository;

import com.example.apienglishapp.entity.NewWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewWordRepository extends JpaRepository<NewWordEntity, Long> {
    boolean existsByEnglishAndVietnamese (String vietnamese, String english);
}

