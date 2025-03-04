package com.lurdharry.ecommerce.notification;

import com.lurdharry.ecommerce.payment.PaymentMethod;

import java.math.BigDecimal;

public record PaymentNotificationRequest(
        String orderReference,

        BigDecimal amount,

        PaymentMethod paymentMethod,

        String customerFirstname,
        String customerLastname,

        String email
) {
}
