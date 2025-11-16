package com.example.clothingandaccessoriesstore.repository;

import com.example.clothingandaccessoriesstore.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findTop15ByOrderByIdDesc();
    List<Product> findAllByOrderByIdDesc(Pageable pageable);
    List<Product> findAllByCategoryId(int categoryId , Pageable pageable);
    List<Product> findAllByName(String productName, Pageable pageable);

    List<Product> findProductByCategory_Id(Integer categoryId);
}
