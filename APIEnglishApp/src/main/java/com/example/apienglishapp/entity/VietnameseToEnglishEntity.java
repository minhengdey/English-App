package com.example.apienglishapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Table(name = "va")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VietnameseToEnglishEntity {
    @Id
    Integer id;
    String word;
    String html;
    String description;
    String pronounce;

}
