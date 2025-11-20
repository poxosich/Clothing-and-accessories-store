package com.example.clothingandaccessoriesstore.cantroller;

import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import com.example.clothingandaccessoriesstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDto> addProduct(@RequestParam("picture") MultipartFile multipartFile,
                                                         @RequestParam() String name,
                                                         @RequestParam() double price,
                                                         @RequestParam() int categoryId,
                                                         @RequestParam() String description,
                                                         @RequestParam() int quantity) {
        ProductResponseDto productResponseDto = productService.addProduct(multipartFile, name, price, categoryId, description, quantity);
        return ResponseEntity.ok(productResponseDto);
    }

    @GetMapping("/get15Product")
    public ResponseEntity<List<ProductResponseDto>> get15Product() {
        return ResponseEntity.ok(productService.findTop15ByOrderByIdDesc());
    }

    @GetMapping("/grtAll")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @DeleteMapping("/delete/{id}")
    private void deleteProductById(@PathVariable Integer id) {
        productService.deleteProductById(id);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestParam() int id,
                                                            @RequestParam("picture") MultipartFile multipartFile,
                                                            @RequestParam() String name,
                                                            @RequestParam() double price,
                                                            @RequestParam() int categoryId,
                                                            @RequestParam() String description,
                                                            @RequestParam() int quantity) {
        ProductResponseDto productResponseDto = productService.updateProduct(id, multipartFile, name, price, categoryId, description, quantity);
        return ResponseEntity.ok(productResponseDto);
    }

    @GetMapping("/getAllByPageable")
    public ResponseEntity<List<ProductResponseDto>> getAllByPageable(@PageableDefault(size = 12) Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProductsByPageable(pageable));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductResponseDto>> getCategoryById(@PathVariable Integer id, @PageableDefault(size = 12) Pageable pageable) {
        List<ProductResponseDto> allProductsByCategoryId = productService.getAllProductsByCategoryId(id, pageable);
        return ResponseEntity.ok(allProductsByCategoryId);
    }

    @GetMapping("/productBy/{name}")
    public ResponseEntity<List<ProductResponseDto>> getProductByName(@PathVariable String name, @PageableDefault(size = 12) Pageable pageable) {
        List<ProductResponseDto> allProductByName = productService.getAllProductByName(name, pageable);
        return ResponseEntity.ok(allProductByName);
    }
}


