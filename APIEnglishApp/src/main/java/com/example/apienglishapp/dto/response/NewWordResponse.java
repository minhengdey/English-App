package com.example.apienglishapp.dto.response;

import com.example.apienglishapp.entity.UserEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewWordResponse {
    Long id;
    String english;
    String vietnamese;
    String topic;
    String date;
    UserEntity user;
}
