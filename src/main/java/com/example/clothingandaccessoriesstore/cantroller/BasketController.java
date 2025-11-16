package com.example.clothingandaccessoriesstore.cantroller;

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

    @PostMapping("/add")
    public ResponseEntity<BasketResponseDto> addBasket(@RequestParam Integer productid, @RequestParam String email) {
        return ResponseEntity.ok(basketService.addBasket(productid, email));
    }

    @GetMapping("/get")
    public ResponseEntity<List<BasketResponseDto>> getBasket(@RequestParam String email) {
        return ResponseEntity.ok(basketService.getBaskedByEmail(email));
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
