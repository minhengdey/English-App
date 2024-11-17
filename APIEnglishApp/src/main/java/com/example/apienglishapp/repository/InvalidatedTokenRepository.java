package com.example.apienglishapp.repository;

import com.example.apienglishapp.entity.InvalidatedTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedTokenEntity, String> {
}
