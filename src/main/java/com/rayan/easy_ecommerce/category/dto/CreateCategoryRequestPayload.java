package com.rayan.easy_ecommerce.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequestPayload(
    @NotBlank String name
) {
}
