package com.lurdharry.ecommerce.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailTemplates {

    PAYMENT_CONFIRMATION("payment-confirmation.html","Payment successfully processed"),
    ORDER_CONFIRMATION("order-confirmation.html","Payment successfully processed");

    private final String template;

    private final String subject;

}
