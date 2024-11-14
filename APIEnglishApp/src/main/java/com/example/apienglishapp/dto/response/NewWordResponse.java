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
    String frontSide;
    String backSide;
    String topic;
    String date;
    String name;
    UserEntity user;
}
