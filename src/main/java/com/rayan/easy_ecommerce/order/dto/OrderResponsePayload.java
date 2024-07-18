package com.rayan.easy_ecommerce.order.dto;

import com.rayan.easy_ecommerce.order.Order;
import com.rayan.easy_ecommerce.order.enums.OrderStatus;
import com.rayan.easy_ecommerce.orderitem.OrderItem;
import com.rayan.easy_ecommerce.payment.Payment;
import com.rayan.easy_ecommerce.user.User;

import java.time.Instant;
import java.util.Set;

public record OrderResponsePayload(
    Long orderId,
    Instant moment,
    OrderStatus status,
    Payment payment,
    User client,
    Set<OrderItem> items,
    Double total
) {
    public static OrderResponsePayload toOrderResponse(Order order) {
        return new OrderResponsePayload(
            order.getId(),
            order.getMoment(),
            order.getStatus(),
            order.getPayment(),
            order.getClient(),
            order.getItems(),
            order.getTotal()
        );
    }
}
