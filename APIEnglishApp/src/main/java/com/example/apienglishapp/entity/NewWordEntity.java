package com.example.apienglishapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Data
@Table (name = "new_word")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewWordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column (name = "english")
    String english;

    @Column (name = "vietnamese")
    String vietnamese;

    @Column (name = "topic")
    String topic;
}