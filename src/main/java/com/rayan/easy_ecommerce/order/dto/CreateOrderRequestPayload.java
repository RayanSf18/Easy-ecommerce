package com.rayan.easy_ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequestPayload(
    @NotBlank String moment,
    @NotBlank String clientName,
    @NotBlank String clientEmail,
    @NotBlank String clientPhone
) {
}
