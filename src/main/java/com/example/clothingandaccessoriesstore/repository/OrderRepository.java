package com.example.clothingandaccessoriesstore.repository;

import com.example.clothingandaccessoriesstore.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByOrderByIdDesc(Pageable pageable);
}
