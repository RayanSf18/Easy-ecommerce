package com.rayan.easy_ecommerce.order;

import com.rayan.easy_ecommerce.infra.exceptions.custom.*;
import com.rayan.easy_ecommerce.order.dto.CreateOrderRequestPayload;
import com.rayan.easy_ecommerce.order.enums.OrderStatus;
import com.rayan.easy_ecommerce.orderitem.OrderItem;
import com.rayan.easy_ecommerce.orderitem.OrderItemService;
import com.rayan.easy_ecommerce.payment.PaymentService;
import com.rayan.easy_ecommerce.payment.dto.CreatePaymentRequestPayload;
import com.rayan.easy_ecommerce.product.ProductService;
import com.rayan.easy_ecommerce.user.User;
import com.rayan.easy_ecommerce.user.UserService;
import com.rayan.easy_ecommerce.user.dto.CreateUserRequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Iterator;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final OrderItemService orderItemService;
    private final PaymentService paymentService;

    public OrderService(OrderRepository orderRepository, UserService userService, ProductService productService, OrderItemService orderItemService, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
        this.orderItemService = orderItemService;
        this.paymentService = paymentService;
    }


    public Long createOrder(CreateOrderRequestPayload payload) {
        try {
            User client = userService.getUser(payload.clientEmail());
            Order newOrder = new Order(null, Instant.parse(payload.moment()), OrderStatus.WAITING_PAYMENT, client);
            orderRepository.save(newOrder);
            return newOrder.getId();
        } catch (UserNotFoundException e) {
            Long clientId = userService.createUser(new CreateUserRequestPayload(payload.clientName(), payload.clientEmail(), payload.clientPhone(), UUID.randomUUID().toString()));
            User clientRegistered = userService.getUser(clientId);
            Order newOrder = new Order(null, Instant.parse(payload.moment()), OrderStatus.WAITING_PAYMENT, clientRegistered);
            orderRepository.save(newOrder);
            return newOrder.getId();
        }
    }


    public Order getOrder(Long orderId) {
        return this.orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
    }


    public Page<Order> getAllOrders(Pageable pageable) {
        return this.orderRepository.findAll(pageable);
    }


    public void addItem(Long orderId, Long productId, Integer quantity) {
        Order order = getOrder(orderId);
        verifyOrderStatus(order);

        try {
            var product = productService.getProduct(productId);
            if (quantity < 1) throw new ProductQuantityInvalidException("The quantity of the product must be at least one unit");
            var orderItem = orderItemService.createOrderItem(order, product, quantity, product.getPrice());
            order.getItems().add(orderItem);
            orderRepository.save(order);
            logger.info("OrderItem successfully added to order.");
        } catch (ProductNotFoundException | OrderItemAlreadyExistsException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }


    public void removeItem(Long orderId, Long productId) {
        Order order = getOrder(orderId);

        verifyOrderStatus(order);

        if (order.getItems().isEmpty()) throw new OrderNotHaveItemsException("This order does not have any items for removal.");

        boolean removed = false;
        Iterator<OrderItem> iterator = order.getItems().iterator();
        while (iterator.hasNext()) {
            OrderItem item = iterator.next();
            if (item.getProduct().getId().equals(productId)) {
                iterator.remove();
                orderItemService.deleteOrderItem(item);
                removed = true;
                break;
            }
        }

        if (!removed) throw new ProductNotFoundException("Product not found in the order.");

        orderRepository.save(order);
        logger.info("OrderItem successfully removed from order.");
    }


    public void addPayment(Long orderId, CreatePaymentRequestPayload payload) {
        Order order = getOrder(orderId);
        verifyOrderStatus(order);

        if (order.getItems().isEmpty()) {
            throw new OrderNotHaveItemsException("This order must have at least one item to make payment.");
        }

        try {
            var newPayment = paymentService.createPayment(order, payload);
            order.setStatus(OrderStatus.PAID);
            order.setPayment(newPayment);
            orderRepository.save(order);
        } catch (PaymentAlreadyExistsException e) {
            logger.error("Payment already exists: {}", e.getMessage());
            throw e;
        }
    }


    public void cancelOrder(Long orderId) {
        Order order = getOrder(orderId);
        verifyOrderStatus(order);
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }


    private void verifyOrderStatus(Order order) {
        if (order.getStatus().equals(OrderStatus.CANCELED)) {
            throw new OrderAlreadyCanceledException("This order has now been cancelled.");
        }
        if (!order.getStatus().equals(OrderStatus.WAITING_PAYMENT)) {
            throw new OrderAlreadyProcessedException("This order cannot be changed anymore, for reasons that it has already been processed.");
        }
    }
}
