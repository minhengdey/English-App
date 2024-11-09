package com.example.demo.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.relational.core.sql.In;

@Entity
@Getter

public class EnglishToVietnamese {
    @Id
    private Integer id;
    private String word;
    private String html;
    private String description;
    private String pronounce;

}
