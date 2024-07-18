package com.rayan.easy_ecommerce.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequestPayload(
    @NotBlank String name,
    @NotBlank String phone,
    @NotBlank String password
) {
}
