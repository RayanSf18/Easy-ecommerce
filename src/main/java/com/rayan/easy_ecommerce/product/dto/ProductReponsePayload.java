package com.rayan.easy_ecommerce.product.dto;

import com.rayan.easy_ecommerce.category.Category;
import com.rayan.easy_ecommerce.product.Product;

import java.util.Set;

public record ProductReponsePayload(
    Long productId,
    String name,
    String brand,
    String model,
    String description,
    Double price,
    String imgUrl,
    Set<Category> categories
) {
    public static ProductReponsePayload toProductResponse(Product product) {
        return new ProductReponsePayload(
            product.getId(),
            product.getName(),
            product.getBrand(),
            product.getModel(),
            product.getDescription(),
            product.getPrice(),
            product.getImgUrl(),
            product.getCategories()
        );
    }
}
