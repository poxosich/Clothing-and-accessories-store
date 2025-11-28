package com.example.clothingandaccessoriesstore.service;

import com.example.clothingandaccessoriesstore.dto.liked.LikedResponseDto;

import java.util.List;

public interface LikedService {
    LikedResponseDto add(int id);
    void deleteLiked(int productId, String email);
    List<LikedResponseDto> getLiked();
}
