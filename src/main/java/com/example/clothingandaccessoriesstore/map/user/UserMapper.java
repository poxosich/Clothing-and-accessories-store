package com.example.clothingandaccessoriesstore.map.user;

import com.example.clothingandaccessoriesstore.dto.user.UserRequest;
import com.example.clothingandaccessoriesstore.dto.user.UserResponse;
import com.example.clothingandaccessoriesstore.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toRequestEntity(UserRequest userRequest);
    UserResponse toDtoResponse(User user);
}
