package com.lurdharry.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CustomerNotFoundException extends RuntimeException {

    private final String msg;
}
