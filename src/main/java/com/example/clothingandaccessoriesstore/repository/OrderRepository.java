package com.example.clothingandaccessoriesstore.repository;

import com.example.clothingandaccessoriesstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
