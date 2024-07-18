package com.rayan.easy_ecommerce.payment.enums;

public enum PaymentMethod {

    CARD(1),
    MONEY(2),
    PIX(3);

    private int code;

    PaymentMethod(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
