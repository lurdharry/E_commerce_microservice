package com.lurdharry.ecommerce.customer;

import com.lurdharry.ecommerce.customer.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
