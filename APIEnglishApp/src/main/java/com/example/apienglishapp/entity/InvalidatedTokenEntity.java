package com.example.apienglishapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invalidated_token")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidatedTokenEntity {
    @Id
    String id;
    @Column (name = "expiryTime")
    Date expiryTime;
}
