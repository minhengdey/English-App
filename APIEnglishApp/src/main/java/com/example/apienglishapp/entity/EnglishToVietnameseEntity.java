package com.example.apienglishapp.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Table(name = "av")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnglishToVietnameseEntity {
    @Id
    Integer id;
    String word;
    String html;
    String description;
    String pronounce;
}
