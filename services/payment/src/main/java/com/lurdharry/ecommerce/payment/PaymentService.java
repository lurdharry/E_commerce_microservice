package com.lurdharry.ecommerce.payment;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;

    public Integer createPayment(@Valid PaymentRequest paymentRequest) {
        var payment = repository.save(mapper.toPayment(paymentRequest));
    }
}
