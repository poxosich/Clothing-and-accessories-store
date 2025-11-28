package com.example.clothingandaccessoriesstore.service.impl;

import com.example.clothingandaccessoriesstore.dto.category.CategoryResponseDto;
import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import com.example.clothingandaccessoriesstore.entity.Category;
import com.example.clothingandaccessoriesstore.entity.Product;
import com.example.clothingandaccessoriesstore.exeption.CategoryNotFoundException;
import com.example.clothingandaccessoriesstore.exeption.ProductNotFoundException;
import com.example.clothingandaccessoriesstore.map.category.CategoryMapper;
import com.example.clothingandaccessoriesstore.map.product.ProductMapper;
import com.example.clothingandaccessoriesstore.repository.BasketRepository;
import com.example.clothingandaccessoriesstore.repository.LikedRepository;
import com.example.clothingandaccessoriesstore.repository.ProductRepository;
import com.example.clothingandaccessoriesstore.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ProductServiceImplTest {
    @Mock
    CategoryService categoryService;
    @Mock
    ProductMapper productMapper;
    @Mock
    CategoryMapper categoryMapper;
    @Mock
    ProductRepository productRepository;
    @Mock
    LikedRepository likedRepository;
    @Mock
    BasketRepository basketRepository;
    @Spy
    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void addProduct() {
        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        String pictureToFolder = "224u53967546794 ars.jpg";
        String name = "fhd";
        double price = 10.00;
        int categoryId = 1;
        String description = "fhd";
        int quantity = 1;
        CategoryResponseDto categoryRequestDto = new CategoryResponseDto();
        Product product = new Product();
        ProductResponseDto productResponseDto = new ProductResponseDto();

        when(productService.addPictureToFolder(multipartFile)).thenReturn(pictureToFolder);
        when(categoryService.getCategoryById(anyInt())).thenReturn(categoryRequestDto);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponseDto(any(Product.class))).thenReturn(productResponseDto);

        ProductResponseDto productResponseDto1 = productService.addProduct(multipartFile, name, price, categoryId, description, quantity);
        assertEquals(productResponseDto, productResponseDto1);
    }


    @Test
    void addPictureToFolderMultipartFileIsNull() {
        MultipartFile multipartFile = null;

        assertThrows(RuntimeException.class, () -> productService.addPictureToFolder(multipartFile));
    }

    @Test
    void addPictureToFolderMultipartFileIsEmpty() {
        MultipartFile multipartFile = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        assertThrows(RuntimeException.class, () -> productService.addPictureToFolder(multipartFile));
    }

    @Test
    void addPictureToFolderMultipartFileTransferToException() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);

        doThrow(new IOException("Disk full")).when(multipartFile).transferTo(any(File.class));

        assertThrows(RuntimeException.class, () -> productService.addPictureToFolder(multipartFile));
    }


    @Test
    void findTop15ByOrderByIdDesc() {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        ProductResponseDto productResponseDto2 = new ProductResponseDto();
        Product product = new Product();
        Product product2 = new Product();

        when(productRepository.findAll()).thenReturn(List.of(product, product2));
        when(productMapper.toResponseDtoList(anyList())).thenReturn(List.of(productResponseDto, productResponseDto2));

        List<ProductResponseDto> top15ByOrderByIdDesc = productService.findTop15ByOrderByIdDesc();

        assertThat(top15ByOrderByIdDesc).isEqualTo(List.of(productResponseDto, productResponseDto2));


    }

    @Test
    void getAllProducts() {
        Product product = new Product();
        Product product2 = new Product();
        ProductResponseDto productResponseDto = new ProductResponseDto();
        ProductResponseDto productResponseDto2 = new ProductResponseDto();
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAllByOrderByIdDesc(pageable)).thenReturn(List.of(product, product2));
        when(productMapper.toResponseDtoList(anyList())).thenReturn(List.of(productResponseDto, productResponseDto2));

        List<ProductResponseDto> allProducts = productService.getAllProducts(pageable);

        assertThat(allProducts).isEqualTo(List.of(productResponseDto, productResponseDto2));
    }

    @Test
    void getProductById() {
        Product product = new Product();
        ProductResponseDto productResponseDto = new ProductResponseDto();

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(productMapper.toResponseDto(product)).thenReturn(productResponseDto);

        ProductResponseDto productById = productService.getProductById(anyInt());

        assertEquals(productResponseDto, productById);
    }

    @Test
    void deleteProductById() {
        Product product = new Product();

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        productService.deleteProductById(anyInt());

        verify(likedRepository, times(1)).deleteByProductId(anyInt());
        verify(basketRepository, times(1)).deleteBasketByProductId(anyInt());
        verify(productRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteProductByIdProductNotFound() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProductById(anyInt()));
    }

    @Test
    void updateProduct() {
        Product product = new Product();
        ProductResponseDto productResponseDto = new ProductResponseDto();
        String pictureToFolder = "224u53967546794_ars.jpg";
        int id = 1;
        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        String name = "fhd";
        double price = 10.00;
        int categoryId = 1;
        String description = "fhd";
        int quantity = 1;
        CategoryResponseDto categoryRequestDto = new CategoryResponseDto();
        Category category = new Category();
        Product newProduct = new Product();

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(categoryService.getCategoryById(categoryId)).thenReturn(categoryRequestDto);

        product.setName(name);
        product.setPrice(price);

        when(productService.addPictureToFolder(multipartFile)).thenReturn(pictureToFolder);

        product.setPicture(productService.addPictureToFolder(multipartFile));

        when(categoryMapper.toEntity2(categoryRequestDto)).thenReturn(category);

        product.setCategory(category);
        product.setDateTime(LocalDateTime.now());
        product.setDescription(description);
        product.setQuantity(quantity);

        when(productRepository.save(product)).thenReturn(newProduct);
        when(productMapper.toResponseDto(newProduct)).thenReturn(productResponseDto);

        ProductResponseDto productResponseDto1 = productService.updateProduct(id, multipartFile, name, price, categoryId, description, quantity);
        assertThat(productResponseDto1).isEqualTo(productResponseDto);
    }

    @Test
    void updateProductCategoryNotFound() {
        int id = 1;
        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        String name = "fhd";
        double price = 10.00;
        int categoryId = 1;
        String description = "fhd";
        int quantity = 1;
        Product product = new Product();

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(categoryService.getCategoryById(categoryId)).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, () -> productService.updateProduct(id, multipartFile, name, price, categoryId, description, quantity));
    }

    @Test
    void getAllProductsByPageable() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Product product = new Product();
        Product product2 = new Product();
        ProductResponseDto productResponseDto = new ProductResponseDto();
        ProductResponseDto productResponseDto2 = new ProductResponseDto();

        when(productRepository.findAllByOrderByIdDesc(pageRequest)).thenReturn(List.of(product, product2));
        when(productMapper.toResponseDtoList(anyList())).thenReturn(List.of(productResponseDto, productResponseDto2));

        List<ProductResponseDto> allProductsByPageable = productService.getAllProductsByPageable(pageRequest);

        assertThat(allProductsByPageable).isEqualTo(List.of(productResponseDto, productResponseDto2));
    }

    @Test
    void getAllProductsByCategoryId() {
        int categoryId = 1;
        PageRequest pageRequest = PageRequest.of(0, 10);
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        Product product = new Product();
        Product product2 = new Product();
        ProductResponseDto productResponseDto = new ProductResponseDto();
        ProductResponseDto productResponseDto2 = new ProductResponseDto();

        when(categoryService.getCategoryById(categoryId)).thenReturn(categoryResponseDto);
        when(productRepository.findProductByCategory_Id(categoryId)).thenReturn(List.of(product, product2));
        when(productMapper.toResponseDtoList(anyList())).thenReturn(List.of(productResponseDto, productResponseDto2));

        List<ProductResponseDto> allProductsByCategoryId = productService.getAllProductsByCategoryId(categoryId, pageRequest);

        assertThat(allProductsByCategoryId).isEqualTo(List.of(productResponseDto, productResponseDto2));
    }

    @Test
    void getAllProductsByCategoryId_CategoryNotFound() {
        int categoryId = 1;
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(categoryService.getCategoryById(categoryId)).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, () -> productService.getAllProductsByCategoryId(categoryId, pageRequest));
    }

    @Test
    void getAllProductByName() {
        String productName = "fhd";
        PageRequest pageRequest = PageRequest.of(0, 10);
        Product product = new Product();
        Product product2 = new Product();
        ProductResponseDto productResponseDto = new ProductResponseDto();
        ProductResponseDto productResponseDto2 = new ProductResponseDto();

        when(productRepository.findAllByName(productName, pageRequest)).thenReturn(List.of(product, product2));
        when(productMapper.toResponseDtoList(anyList())).thenReturn(List.of(productResponseDto, productResponseDto2));

        List<ProductResponseDto> allProductByName = productService.getAllProductByName(productName, pageRequest);

        assertThat(allProductByName).isEqualTo(List.of(productResponseDto, productResponseDto2));
    }
}