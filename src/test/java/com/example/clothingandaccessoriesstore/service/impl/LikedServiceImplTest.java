package com.example.clothingandaccessoriesstore.service.impl;

import com.example.clothingandaccessoriesstore.dto.liked.LikedResponseDto;
import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import com.example.clothingandaccessoriesstore.entity.Liked;
import com.example.clothingandaccessoriesstore.entity.Product;
import com.example.clothingandaccessoriesstore.entity.User;
import com.example.clothingandaccessoriesstore.exeption.ProductNotFoundException;
import com.example.clothingandaccessoriesstore.exeption.UserNotFoundException;
import com.example.clothingandaccessoriesstore.map.liked.LikedMapper;
import com.example.clothingandaccessoriesstore.map.product.ProductMapper;
import com.example.clothingandaccessoriesstore.repository.LikedRepository;
import com.example.clothingandaccessoriesstore.service.ProductService;
import com.example.clothingandaccessoriesstore.service.UserService;
import com.example.clothingandaccessoriesstore.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikedServiceImplTest {
    @Mock
    ProductService productService;
    @Mock
    UserService userService;
    @Mock
    LikedRepository likedRepository;
    @Mock
    ProductMapper productMapper;
    @Mock
    LikedMapper likedMapper;
    @Mock
    JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    LikedServiceImpl likedServiceImpl;

    @Test
    void deleteLiked() {
        int productId = 1;
        String email = "cholakyanars4@gmail.com";
        Liked liked = new Liked();

        when(likedRepository.findByProductIdAndUserEmail(productId, email)).thenReturn(Optional.of(liked));

        likedServiceImpl.deleteLiked(productId, email);

        verify(likedRepository).deleteByProductIdAndUserEmail(productId, email);
    }

    @Test
    void deleteLikedProductNotFound() {
        int productId = 1;
        String email = "cholakyanars4@gmail.com";

        when(likedRepository.findByProductIdAndUserEmail(productId, email)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> likedServiceImpl.deleteLiked(productId, email));
    }


    @Test
    void getLiked() {
        String email = "cholakyanars4@gmail.com";
        Liked liked = new Liked();
        Liked liked2 = new Liked();
        LikedResponseDto likedResponseDto = new LikedResponseDto();
        LikedResponseDto likedResponseDto2 = new LikedResponseDto();

        when(jwtTokenUtil.getCurrentUserEmail()).thenReturn(email);
        when(likedRepository.findLikedByUserEmail(email)).thenReturn(List.of(liked, liked2));
        when(likedMapper.toResponseDtoList(List.of(liked, liked2))).thenReturn(List.of(likedResponseDto, likedResponseDto2));

        List<LikedResponseDto> responseEntity = likedServiceImpl.getLiked();

        assertThat(responseEntity).isEqualTo(List.of(likedResponseDto, likedResponseDto2));
    }


    @Test
    void add() {
        int productId = 1;
        String email = "cholakyanars4@gmail.com";

        User user = new User();
        ProductResponseDto productDto = new ProductResponseDto();
        Product product = new Product();
        Liked saved = new Liked();
        LikedResponseDto response = new LikedResponseDto();

        when(jwtTokenUtil.getCurrentUserEmail()).thenReturn(email);
        when(likedRepository.findByProductIdAndUserEmail(productId, email)).thenReturn(Optional.empty());
        when(userService.findUserByEmail(email)).thenReturn(user);
        when(productService.getProductById(productId)).thenReturn(productDto);
        when(productMapper.fromResponseDto(productDto)).thenReturn(product);
        when(likedRepository.save(any(Liked.class))).thenReturn(saved);
        when(likedMapper.toResponseDto(saved)).thenReturn(response);

        LikedResponseDto result = likedServiceImpl.add(productId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void addLikedIsPresent() {
        int productId = 1;
        String email = "cholakyanars4@gmail.com";
        Liked liked = new Liked();
        LikedResponseDto likedResponseDto = new LikedResponseDto();

        when(jwtTokenUtil.getCurrentUserEmail()).thenReturn(email);
        when(likedRepository.findByProductIdAndUserEmail(productId, email)).thenReturn(Optional.of(liked));
        when(likedMapper.toResponseDto(liked)).thenReturn(likedResponseDto);

        LikedResponseDto add = likedServiceImpl.add(productId);

        verify(likedRepository).delete(liked);

        assertThat(add).isEqualTo(likedResponseDto);
    }


    @Test
    void addUserNotFound() {
        int productId = 1;
        String email = "cholakyanars4@gmail.com";

        when(jwtTokenUtil.getCurrentUserEmail()).thenReturn(email);
        when(userService.findUserByEmail(email)).thenThrow(new UserNotFoundException(email));

        assertThrows(UserNotFoundException.class, () -> likedServiceImpl.add(productId));
    }

    @Test
    void addProductNotFound() {
        int productId = 1;
        String email = "cholakyanars4@gmail.com";
        User user = new User();

        when(jwtTokenUtil.getCurrentUserEmail()).thenReturn(email);
        when(userService.findUserByEmail(email)).thenReturn(user);
        when(productService.getProductById(productId)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> likedServiceImpl.add(productId));

        verify(likedRepository, never()).save(any(Liked.class));
    }
}
