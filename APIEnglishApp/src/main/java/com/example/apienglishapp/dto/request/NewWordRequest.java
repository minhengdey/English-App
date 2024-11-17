package com.example.apienglishapp.dto.request;

import com.example.apienglishapp.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewWordRequest {
    String frontSide;
    String backSide;
    String topic;
    String date;
    String name;
    UserEntity user;
}
