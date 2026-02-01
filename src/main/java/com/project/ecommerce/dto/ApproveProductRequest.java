package com.project.ecommerce.dto;


import lombok.Data;

@Data
public class ApproveProductRequest {
    private String description;
    private Long categoryId;
    private String barcode;
    private String name;
    private String brand;
    private String imageUrl;
    private Double calories;
    private String nutrientsJson;
    private Long estimatedPrice;
}
