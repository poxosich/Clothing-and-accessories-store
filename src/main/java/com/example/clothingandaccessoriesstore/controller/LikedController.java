package com.example.clothingandaccessoriesstore.controller;

import com.example.clothingandaccessoriesstore.dto.liked.LikedResponseDto;
import com.example.clothingandaccessoriesstore.service.LikedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/liked")
@RequiredArgsConstructor
public class LikedController {
    private final LikedService likedService;

    @PostMapping()
    public LikedResponseDto addLiked(@RequestParam int productId) {
        return likedService.add(productId);
    }

    @GetMapping("/get")
    public ResponseEntity<List<LikedResponseDto>> getLiked() {
        return ResponseEntity.ok(likedService.getLiked());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam int productId, @RequestParam String email) {
        likedService.deleteLiked(productId, email);
        return ResponseEntity.ok("deleted");
    }
}
