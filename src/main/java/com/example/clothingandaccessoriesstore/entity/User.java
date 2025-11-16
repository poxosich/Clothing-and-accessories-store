package com.example.clothingandaccessoriesstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Integer id;
 private String name;
 private String surname;
 private String email;
 private String password;
 private String token;
 private boolean active;
 @Enumerated(EnumType.STRING)
 private Role role;
 private String picture;
}
