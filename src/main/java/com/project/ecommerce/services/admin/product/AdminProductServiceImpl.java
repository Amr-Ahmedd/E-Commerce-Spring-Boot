package com.project.ecommerce.services.admin.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecommerce.dto.ApproveProductRequest;
import com.project.ecommerce.dto.OpenFoodFactsProductDto;
import com.project.ecommerce.entity.Category;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.repository.CategoryRepository;
import com.project.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RestClient restClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void deleteApprovedProduct(Long id) {
        // If you want ONLY approved products to be deletable, you can check that here.
        // For now: just delete if exists.
        productRepository.deleteById(id);
    }


    @Override
    public List<OpenFoodFactsProductDto> searchOpenFoodFacts(String query) {

        // OpenFoodFacts search API (name search)
        String url =
                "https://world.openfoodfacts.org/cgi/search.pl" +
                        "?search_terms=" + query +
                        "&search_simple=1" +
                        "&action=process" +
                        "&json=1" +
                        "&page_size=20";

        String jsonResponse = restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode products = root.get("products");

            List<OpenFoodFactsProductDto> result = new ArrayList<>();
            if (products == null || !products.isArray()) return result;

            for (JsonNode p : products) {

                String barcode = textOrNull(p, "code");
                String name = textOrNull(p, "product_name");
                String brand = textOrNull(p, "brands");
                String imageUrl = textOrNull(p, "image_url");

                // nutriments section has calories and other nutrients
                JsonNode nutriments = p.get("nutriments");

                Double calories = null;
                String nutrientsJson = "{}";

                if (nutriments != null && nutriments.isObject()) {
                    // Common calorie field: "energy-kcal_100g"
                    calories = doubleOrNull(nutriments, "energy-kcal_100g");
                    if (calories == null) {
                        calories = doubleOrNull(nutriments, "energy-kcal");
                    }

                    // Store a small JSON summary (you can expand later)
                    nutrientsJson = objectMapper.createObjectNode()
                            .put("fat_100g", doubleOrZero(nutriments, "fat_100g"))
                            .put("carbohydrates_100g", doubleOrZero(nutriments, "carbohydrates_100g"))
                            .put("proteins_100g", doubleOrZero(nutriments, "proteins_100g"))
                            .put("sugars_100g", doubleOrZero(nutriments, "sugars_100g"))
                            .put("salt_100g", doubleOrZero(nutriments, "salt_100g"))
                            .toString();
                }

                // Skip empty items (OpenFoodFacts sometimes returns incomplete rows)
                if (barcode == null || name == null || name.isBlank()) continue;

                OpenFoodFactsProductDto dto = new OpenFoodFactsProductDto();
                dto.setBarcode(barcode);
                dto.setName(name);
                dto.setBrand(brand);
                dto.setImageUrl(imageUrl);
                dto.setCalories(calories);

                dto.setNutrientsJson(nutrientsJson);

                // random price (backend-side) - simple stable random by barcode hash
                dto.setEstimatedPrice(stableRandomPrice(barcode));

                result.add(dto);
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OpenFoodFacts response", e);
        }
    }

    @Override
    public Product approve(ApproveProductRequest req) {
        Category category = null;
        if (req.getCategoryId() != null) {
            category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        Product product = productRepository.findByBarcode(req.getBarcode())
                .orElse(new Product());

        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setBrand(req.getBrand());
        product.setImageUrl(req.getImageUrl());
        product.setCalories(req.getCalories());
        product.setNutrientsJson(req.getNutrientsJson());
        product.setBarcode(req.getBarcode());
        product.setCategory(category);

        product.setPrice(req.getEstimatedPrice() != null ? req.getEstimatedPrice() : 50L);
        product.setApproved(true);

        return productRepository.save(product);
    }

    // ---------------- helpers ----------------

    private String textOrNull(JsonNode node, String field) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) return null;
        return v.asText();
    }

    private Double doubleOrNull(JsonNode node, String field) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) return null;
        if (v.isNumber()) return v.asDouble();
        try {
            return Double.parseDouble(v.asText());
        } catch (Exception e) {
            return null;
        }
    }

    private double doubleOrZero(JsonNode node, String field) {
        Double d = doubleOrNull(node, field);
        return d == null ? 0.0 : d;
    }

    private long stableRandomPrice(String barcode) {
        // Generates price between 20 and 200 (EGP-style range)
        Random r = new Random(barcode.hashCode());
        return 20 + r.nextInt(181);
    }
}
