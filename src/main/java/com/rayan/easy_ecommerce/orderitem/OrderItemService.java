package com.rayan.easy_ecommerce.orderitem;

import com.rayan.easy_ecommerce.infra.exceptions.custom.OrderItemAlreadyExistsException;
import com.rayan.easy_ecommerce.order.Order;
import com.rayan.easy_ecommerce.product.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderItemService {


    private final OrderItemRepository orderItemRepository;


    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }


    public OrderItem createOrderItem(Order order, Product product, Integer quantity, Double price) {
        Optional<OrderItem> existingOrderItem = orderItemRepository.findByOrderIdAndProductId(order.getId(), product.getId());

        if (existingOrderItem.isPresent()) return updateOrderItemQuantity(existingOrderItem.get(), quantity);

        return saveNewOrderItem(order, product, quantity, price);
    }


    public void deleteOrderItem(OrderItem orderItem) {
        orderItemRepository.delete(orderItem);
    }


    private OrderItem updateOrderItemQuantity(OrderItem orderItem, Integer quantity) {
        if (orderItem.getQuantity().equals(quantity)) {
            throw new OrderItemAlreadyExistsException("There is already an order item registered with this order_id and product_id.");
        }
        orderItem.setQuantity(quantity);
        return orderItemRepository.save(orderItem);
    }


    private OrderItem saveNewOrderItem(Order order, Product product, Integer quantity, Double price) {
        OrderItem newOrderItem = new OrderItem(order, product, quantity, price);
        return orderItemRepository.save(newOrderItem);
    }




}
