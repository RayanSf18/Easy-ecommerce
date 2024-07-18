package com.rayan.easy_ecommerce.order;

import com.rayan.easy_ecommerce.order.dto.CreateOrderRequestPayload;
import com.rayan.easy_ecommerce.order.dto.OrderIdResponsePayload;
import com.rayan.easy_ecommerce.order.dto.OrderResponsePayload;
import com.rayan.easy_ecommerce.payment.dto.CreatePaymentRequestPayload;
import com.rayan.easy_ecommerce.product.dto.ProductIdResponsePayload;
import com.rayan.easy_ecommerce.product.dto.ProductReponsePayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/orders", produces = {"application/json"})
@Tag(name = "Orders")
public class OrderController {


    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create a new Order", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderIdResponsePayload.class))),
        @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderIdResponsePayload> createOrder(@Valid @RequestBody CreateOrderRequestPayload payload) {
        Long orderId = orderService.createOrder(payload);
        URI location = buildLocationUri(orderId);
        return ResponseEntity.created(location).body(new OrderIdResponsePayload(orderId));
    }

    @Operation(summary = "Add product to order", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product added successfully"),
        @ApiResponse(responseCode = "422", description = "Quantity not valid", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "422", description = "Order canceled", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "422", description = "Order already placed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(value = "/{orderId}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addItem(@PathVariable("orderId") Long orderId, @RequestParam Long productId, @RequestParam Integer quantity) {
        orderService.addItem(orderId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove product to order", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product removed successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "422", description = "Order canceled", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "422", description = "Order already placed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Order has no products", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(value = "/{orderId}/items/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeItem(@PathVariable Long orderId,  @RequestParam Long productId) {
        orderService.removeItem(orderId, productId);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Add payment to order", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product removed successfully"),
        @ApiResponse(responseCode = "422", description = "Order canceled", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "422", description = "Order already placed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Order has no products", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<Void> addPayment(@PathVariable("orderId") Long orderId, @Valid @RequestBody CreatePaymentRequestPayload payload) {
        orderService.addPayment(orderId, payload);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Search for a specific order", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponsePayload.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponsePayload> getOrder(@PathVariable("orderId") Long orderId) {
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok().body(OrderResponsePayload.toOrderResponse(order));
    }

    @Operation(summary = "Search all orders", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponsePayload.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<OrderResponsePayload>> getAllOrders(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Order> orders = orderService.getAllOrders(pageRequest);
        return ResponseEntity.ok().body(orders.map(OrderResponsePayload::toOrderResponse));
    }

    @Operation(summary = "Cancel a specific order", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order canceled successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "422", description = "Order canceled", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "422", description = "Order already placed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }


    private URI buildLocationUri(Long orderId) {
        return ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(orderId)
            .toUri();
    }
}
