package com.lurdharry.ecommerce.payment;

import com.lurdharry.ecommerce.customer.CustomerResponse;
import com.lurdharry.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(

        BigDecimal amount,

        PaymentMethod paymentMethod,

        Integer orderId,

        String orderReference,

        CustomerResponse customer
) {
}
