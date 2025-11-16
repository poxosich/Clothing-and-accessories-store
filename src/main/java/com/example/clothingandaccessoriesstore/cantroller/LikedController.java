package com.example.clothingandaccessoriesstore.cantroller;

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

    @PostMapping("/add")
    public LikedResponseDto addLiked(@RequestParam int productId, @RequestParam String email) {
        return likedService.add(productId, email);
    }

    @GetMapping("/get/{email}")
    public ResponseEntity<List<LikedResponseDto>> getLiked(@PathVariable String email) {
        return ResponseEntity.ok(likedService.getLiked(email));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam int productId, @RequestParam String email) {
        likedService.deleteLiked(productId, email);
        return ResponseEntity.ok("deleted");
    }

}
