package com.rayan.easy_ecommerce.payment;

import com.rayan.easy_ecommerce.infra.exceptions.custom.PaymentAlreadyExistsException;
import com.rayan.easy_ecommerce.order.Order;
import com.rayan.easy_ecommerce.payment.dto.CreatePaymentRequestPayload;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class PaymentService {


    private final PaymentRepository paymentRepository;


    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    public Payment createPayment(Order order, CreatePaymentRequestPayload payload) {
        validatePaymentDoesNotExist(order.getId());
        return new Payment(null, Instant.parse(payload.moment()), payload.paymentMethod(), payload.amount(), order);
    }


    private void validatePaymentDoesNotExist(Long orderId) {
        Optional<Payment> existingPayment = paymentRepository.findById(orderId);
        if (existingPayment.isPresent()) {
            throw new PaymentAlreadyExistsException("Payment for this order has already been made");
        }
    }


}
