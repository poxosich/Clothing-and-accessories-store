package com.example.clothingandaccessoriesstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private double price;
    private String picture;
    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
    private boolean active;
    @Column(columnDefinition = "TEXT")
    private String description;
    private int quantity;
}
