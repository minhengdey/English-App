package com.example.apienglishapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String username;

    String password;

    String avatar;

    String name;

    int day;

    int month;

    int year;

    String gender;

    String email;

    String phone;
}
