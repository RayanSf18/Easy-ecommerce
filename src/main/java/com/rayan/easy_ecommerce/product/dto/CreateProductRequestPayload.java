package com.rayan.easy_ecommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateProductRequestPayload(
    @NotBlank String name,
    @NotBlank String brand,
    @NotBlank String model,
    @NotBlank String description,
    @NotNull Double price,
    @NotBlank String imgUrl,
    @NotNull Set<String> categories
) {
}
