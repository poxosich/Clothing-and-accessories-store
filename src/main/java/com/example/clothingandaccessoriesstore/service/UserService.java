package com.example.clothingandaccessoriesstore.service;

import com.example.clothingandaccessoriesstore.dto.user.AuthorizationRequest;
import com.example.clothingandaccessoriesstore.dto.user.AuthorizationResponse;
import com.example.clothingandaccessoriesstore.dto.user.UserRequest;
import com.example.clothingandaccessoriesstore.dto.user.UserResponse;
import com.example.clothingandaccessoriesstore.entity.User;

public interface UserService {
User findUserByEmail(String email);
UserResponse inspectionUserRegister(UserRequest userRequest);
UserResponse saveUser(UserRequest userRequest);
void findUserByToken(String token);
AuthorizationResponse inspectionUserLogin(AuthorizationRequest authorizationRequest);


}
