package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductResponseDto;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping("/products")
    public List<ProductResponseDto> getApprovedProducts() {
        return productRepository.findAllByApprovedTrue()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/products/{id}")
    public ProductResponseDto getProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        return toDto(product);
    }

    private ProductResponseDto toDto(Product p) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setBrand(p.getBrand());
        dto.setImageUrl(p.getImageUrl());
        dto.setPrice(p.getPrice());
        dto.setCalories(p.getCalories());
        dto.setNutrientsJson(p.getNutrientsJson());

        if (p.getCategory() != null) {
            dto.setCategoryId(p.getCategory().getId());
            dto.setCategoryName(p.getCategory().getName());
        }

        return dto;
    }
}
