package com.example.clothingandaccessoriesstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ClothingAndAccessoriesStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClothingAndAccessoriesStoreApplication.class, args);
    }

}
