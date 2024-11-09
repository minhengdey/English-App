package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class VietnameseToEnglish {
    @Id
    private Integer id;
    private String word;
    private String html;
    private String description;
    private String pronounce;

}
