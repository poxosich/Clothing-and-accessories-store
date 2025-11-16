package com.example.clothingandaccessoriesstore.repository;

import com.example.clothingandaccessoriesstore.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Integer> {
       Optional<Basket> findBasketByProductIdAndUserEmail(int productid, String email);
       List<Basket> findBasketByUserEmail(String userEmail);
       void deleteBasketByProductIdAndUserEmail(int productid, String userEmail);
}
