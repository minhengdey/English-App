package com.example.apienglishapp.repository;

import com.example.apienglishapp.entity.NewWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewWordRepository extends JpaRepository<NewWordEntity, Long> {
    Optional<NewWordEntity> findByUserIdAndId (Long userId, Long id);
}

