package com.example.apienglishapp.repository;

import com.example.apienglishapp.entity.NewWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewWordRepository extends JpaRepository<NewWordEntity, Long> {
    boolean existsByFrontSideAndBackSide (String frontSide, String backSide);
    Optional<NewWordEntity> findByUserIdAndId (Long userId, Long id);
    void deleteByUserIdAndId (Long userId, Long id);
}
