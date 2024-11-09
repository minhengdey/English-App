package com.example.apienglishapp.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;

    String name;

    int day;

    int month;

    int year;

    String gender;

    String email;

    String phone;
}
