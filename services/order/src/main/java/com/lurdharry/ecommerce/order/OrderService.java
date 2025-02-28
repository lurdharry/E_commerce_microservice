package com.lurdharry.ecommerce.order;


import com.lurdharry.ecommerce.customer.CustomerClient;
import com.lurdharry.ecommerce.exception.BusinessException;
import com.lurdharry.ecommerce.product.ProductClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;

    public Integer createOrder(@Valid OrderRequest orderRequest) {

        // check the customer -->openFeign
        var customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order: No customer exists with the ID::"));

        // purchase the product
        productClient.purchaseProducts(orderRequest.products());

        var order =

        //persist order

        //persist order lines

        // start payment process

        // send order notification --> notification -ms
        return  null;
    }
}
