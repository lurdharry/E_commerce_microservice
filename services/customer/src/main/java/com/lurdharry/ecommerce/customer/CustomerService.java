package com.lurdharry.ecommerce.customer;

import com.lurdharry.ecommerce.exception.CustomerNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                                "Cannot update customer:: no customer found with the ID:: %s", request.id()
                        )
                ));
        customerMapper.mergeCustomer(customer, request);
        customerRepository.save(customer);
    }

    public List<CustomerResponse> findAllCustomer() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean existsById(String customerId) {

        return customerRepository.findById(customerId).isPresent();
    }

    public CustomerResponse findById(String customerId) {

        return customerRepository.findById(customerId)
                .map(customerMapper::fromCustomer)
                .orElseThrow(()-> new CustomerNotFoundException(
                        format("No customer found with the provided ID:: %s", customerId)
                ));
    }

    public void deleteCustomer(String customerId) {

        customerRepository.deleteById(customerId);
    }
}
