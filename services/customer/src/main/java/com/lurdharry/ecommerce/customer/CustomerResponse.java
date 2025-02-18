package com.lurdharry.ecommerce.customer;

import com.lurdharry.ecommerce.customer.models.Address;

public record CustomerResponse(

        String id,

        String firstname,

        String lastname,

        String email,

        Address address
) {

}
