package com.project.ecommerce.controller.admin;

import com.project.ecommerce.dto.ApproveProductRequest;
import com.project.ecommerce.dto.OpenFoodFactsProductDto;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.services.admin.product.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping("/products/search")
    public ResponseEntity<List<OpenFoodFactsProductDto>> search(@RequestParam String query) {
        return ResponseEntity.ok(adminProductService.searchOpenFoodFacts(query));
    }

    @PostMapping("/products/approve")
    public ResponseEntity<Product> approve(@RequestBody ApproveProductRequest req) {
        return ResponseEntity.ok(adminProductService.approve(req));
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminProductService.deleteApprovedProduct(id);
        return ResponseEntity.noContent().build(); // 204
    }

}