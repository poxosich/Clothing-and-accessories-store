package com.example.clothingandaccessoriesstore.repository;

import com.example.clothingandaccessoriesstore.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikedRepository extends JpaRepository<Liked, Integer> {
    void deleteByProductIdAndUserEmail(int productId, String email);
    Optional<Liked> findByProductIdAndUserEmail(int productId, String email);
    List<Liked> findLikedByUserEmail(String email);
}
