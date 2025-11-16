package com.example.clothingandaccessoriesstore.dto.user;

import com.example.clothingandaccessoriesstore.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String passwordDuplicate;
    private String token;
    private Boolean active;
    private Role role;
    private String picture;
}
