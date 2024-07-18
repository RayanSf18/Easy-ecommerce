package com.rayan.easy_ecommerce.order.enums;

public enum OrderStatus {
    WAITING_PAYMENT(1),
    PAID(2),
    SHIPPED(3),
    DELIVERED(4),
    CANCELED(5);

    private int code;

    OrderStatus(int code) {
        this.code = code;
    }

    public static OrderStatus valueOf(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (code == status.getCode()) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus code");
    }

    public int getCode() {
        return code;
    }


}
