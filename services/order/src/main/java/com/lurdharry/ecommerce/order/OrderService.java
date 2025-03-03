package com.lurdharry.ecommerce.order;


import com.lurdharry.ecommerce.customer.CustomerClient;
import com.lurdharry.ecommerce.exception.BusinessException;
import com.lurdharry.ecommerce.kafta.OrderConfirmation;
import com.lurdharry.ecommerce.kafta.OrderProducer;
import com.lurdharry.ecommerce.orderline.OrderLineRequest;
import com.lurdharry.ecommerce.orderline.OrderLineService;
import com.lurdharry.ecommerce.product.ProductClient;
import com.lurdharry.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public Integer createOrder(@Valid OrderRequest orderRequest) {

        // check the customer -->openFeign
        var customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order: No customer exists with the ID::"));

        // purchase the product
        var purchasedProducts = productClient.purchaseProducts(orderRequest.products());

        //persist order
        var order = orderRepository.save(mapper.toOrder(orderRequest));

        //persist order lines

        for (PurchaseRequest purchaseRequest: orderRequest.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        //todo start payment process

        // send order notification --> notification -ms

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        orderRequest.reference(),
                        orderRequest.amount(),
                        orderRequest.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return  order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {

        return orderRepository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(()-> new EntityNotFoundException(String.format("No order found with the provided ID: %d", orderId)));
    }
}
