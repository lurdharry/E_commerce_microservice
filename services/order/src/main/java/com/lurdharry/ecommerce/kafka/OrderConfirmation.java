package com.lurdharry.ecommerce.kafka;

import com.lurdharry.ecommerce.customer.CustomerResponse;
import com.lurdharry.ecommerce.order.PaymentMethod;
import com.lurdharry.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,

        BigDecimal totalAmount,

        PaymentMethod paymentMethod,

        CustomerResponse customer,

        List<PurchaseResponse> products
) {
}
