package com.project.ecommerce.services.admin.product;

import com.project.ecommerce.dto.ApproveProductRequest;
import com.project.ecommerce.dto.OpenFoodFactsProductDto;
import com.project.ecommerce.entity.Product;

import java.util.List;

public interface AdminProductService {
    List<OpenFoodFactsProductDto> searchOpenFoodFacts(String query);
    Product approve(ApproveProductRequest req);
    void deleteApprovedProduct(Long id);


}
