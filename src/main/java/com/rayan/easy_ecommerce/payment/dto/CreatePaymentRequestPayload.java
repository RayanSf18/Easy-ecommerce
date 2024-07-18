package com.rayan.easy_ecommerce.payment.dto;

import com.rayan.easy_ecommerce.payment.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequestPayload(
    @NotBlank String moment,
    @NotNull PaymentMethod paymentMethod,
    @NotNull Double amount
) {
}
