package com.example.apienglishapp.dto.response;

import com.example.apienglishapp.entity.NewWordEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;

    String username;

    String name;

    int day;

    int month;

    int year;

    String gender;

    String email;

    String phone;

    Set<String> roles;

    Set<NewWordEntity> newWords;
}
