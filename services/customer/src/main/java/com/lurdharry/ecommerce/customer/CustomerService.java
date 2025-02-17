package com.lurdharry.ecommerce.customer;

import com.lurdharry.ecommerce.customer.models.Customer;
import com.lurdharry.ecommerce.exception.CustomerNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

   private final CustomerRepository customerRepository;
   private final CustomerMapper customerMapper;

    public String createCustomer(@Valid CustomerRequest request) {
        var customer =  customerMapper.toCustomer(request);
        return  customerRepository.save(customer).getId();
    }

    public void updateCustomer(@Valid CustomerRequest request) {
        var customer =  customerRepository.findById(request.id())
                .orElseThrow(()-> new CustomerNotFoundException(
                        format(
                                "Cannot update customer:: no customer found with the ID:: %S", request.id()
                        )
                ));
        customerMapper.mergeCustomer(customer, request);
        customerRepository.save(customer);
    }
}
