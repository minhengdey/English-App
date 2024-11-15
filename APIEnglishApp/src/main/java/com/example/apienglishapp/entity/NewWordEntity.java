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

    @Column (name = "front_side", columnDefinition = "TEXT")
    String frontSide;

    @Column (name = "back_side", columnDefinition = "TEXT")
    String backSide;

    @Column (name = "topic")
    String topic;

    @Column (name = "date")
    String date;

    @Column (name = "name")
    String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;
}