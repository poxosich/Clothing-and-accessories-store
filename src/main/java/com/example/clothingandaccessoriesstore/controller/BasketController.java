package com.example.clothingandaccessoriesstore.controller;

import com.example.clothingandaccessoriesstore.dto.basket.BasketRequestDto;
import com.example.clothingandaccessoriesstore.dto.basket.BasketResponseDto;
import com.example.clothingandaccessoriesstore.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @PostMapping()
    public ResponseEntity<BasketResponseDto> addBasket(@RequestParam Integer productid) {
        return ResponseEntity.ok(basketService.addBasket(productid));
    }

    @GetMapping("/get")
    public ResponseEntity<List<BasketResponseDto>> getBasket() {
        return ResponseEntity.ok(basketService.getBaskedByEmail());
    }

    @DeleteMapping("/delete")
    public void deleteBasket(@RequestParam int productId, @RequestParam String email) {
        basketService.deleteBasket(productId, email);
    }

    @PutMapping("/update")
    public void updateBasket(@RequestBody BasketRequestDto basketRequestDto) {
        basketService.updateBasket(basketRequestDto);
    }
}
