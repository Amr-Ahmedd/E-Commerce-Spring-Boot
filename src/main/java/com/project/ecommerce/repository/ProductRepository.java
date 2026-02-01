package com.project.ecommerce.repository;

import com.project.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByApprovedTrue();
    Optional<Product> findByBarcode(String barcode);
    Optional<Product> findByIdAndApprovedTrue(Long id);
}
