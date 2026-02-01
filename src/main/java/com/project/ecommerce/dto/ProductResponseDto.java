package com.project.ecommerce.dto;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class ProductResponseDto {

    private Long id;
    private String name;
    private String brand;
    private String imageUrl;
    private Long price;
    private Double calories;
    private String nutrientsJson;

    private Long categoryId;
    private String categoryName;
}
