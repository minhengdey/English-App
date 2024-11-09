package com.example.apienglishapp.dto.request;

import com.example.apienglishapp.entity.UserEntity;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewWordRequest {
    String english;
    String vietnamese;
    String topic;
    String date;
    UserEntity user;
}
