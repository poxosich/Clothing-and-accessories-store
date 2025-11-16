package com.example.clothingandaccessoriesstore.service.impl;

import com.example.clothingandaccessoriesstore.dto.liked.LikedRequestDto;
import com.example.clothingandaccessoriesstore.dto.liked.LikedResponseDto;
import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import com.example.clothingandaccessoriesstore.entity.Liked;
import com.example.clothingandaccessoriesstore.entity.User;
import com.example.clothingandaccessoriesstore.exeption.LikedDublinException;
import com.example.clothingandaccessoriesstore.exeption.ProductNotFoundException;
import com.example.clothingandaccessoriesstore.exeption.UserNotFoundException;
import com.example.clothingandaccessoriesstore.map.liked.LikedMapper;
import com.example.clothingandaccessoriesstore.map.product.ProductMapper;
import com.example.clothingandaccessoriesstore.repository.LikedRepository;
import com.example.clothingandaccessoriesstore.service.LikedService;
import com.example.clothingandaccessoriesstore.service.ProductService;
import com.example.clothingandaccessoriesstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikedServiceImpl implements LikedService {
    private final ProductService productService;
    private final UserService userService;
    private final LikedRepository likedRepository;
    private final ProductMapper productMapper;
    private final LikedMapper likedMapper;

    @Transactional
    public void deleteLiked(int productId, String email) {
        Optional<Liked> byProductIdAndUserEmail = likedRepository.findByProductIdAndUserEmail(productId, email);
        if (byProductIdAndUserEmail.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }
        likedRepository.deleteByProductIdAndUserEmail(productId, email);
    }

    @Override
    public List<LikedResponseDto> getLiked(String email) {
        List<Liked> likedByUserEmail = likedRepository.findLikedByUserEmail(email);
        return likedMapper.toResponseDtoList(likedByUserEmail);
    }

    @Transactional
    @Override
    public LikedResponseDto add(int id, String email) {
        Optional<Liked> existingLike = likedRepository.findByProductIdAndUserEmail(id, email);
        if (existingLike.isPresent()) {
            likedRepository.delete(existingLike.get());
            return likedMapper.toResponseDto(existingLike.get());
        }
        try {
            User user = userService.findUserByEmail(email);
            ProductResponseDto productDto = productService.getProductById(id);
            if (productDto == null) {
                throw new ProductNotFoundException("Product not found");
            }
            Liked saved = likedRepository.save(
                    Liked.builder()
                            .product(productMapper.fromResponseDto(productDto))
                            .user(user)
                            .build());
            return likedMapper.toResponseDto(saved);
        } catch (Exception e) {
            throw new UserNotFoundException(email);
        }
    }
}
