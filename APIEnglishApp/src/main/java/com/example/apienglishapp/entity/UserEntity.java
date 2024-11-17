package com.example.apienglishapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;

    @Column (name = "username")
    String username;

    @Column (name = "password")
    String password;

    @Column (name = "name")
    String name;

    @Column (name = "day")
    int day;

    @Column (name = "month")
    int month;

    @Column (name = "year")
    int year;

    @Column (name = "gender")
    String gender;

    @Column (name = "email")
    String email;

    @Column (name = "phone")
    String phone;

    @Column (name = "login_method")
    String loginMethod;

    @ElementCollection
    @Column (name = "roles")
    Set<String> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<NewWordEntity> newWords = new HashSet<>();
}
