package com.lurdharry.ecommerce.kafka.payment;

import java.math.BigDecimal;

public record PaymentConfirmation(
        String oderReference,

        BigDecimal amount,

        PaymentMethod paymentMethod,

        String customerFirstname,

        String customerLastname,

        String customerEmail
) {
}
