package com.rayan.easy_ecommerce.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestPayload(
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank String phone,
    @NotBlank String password
) {
}
