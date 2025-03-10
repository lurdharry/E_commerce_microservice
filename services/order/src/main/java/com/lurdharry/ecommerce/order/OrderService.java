package com.lurdharry.ecommerce.order;


import com.lurdharry.ecommerce.customer.CustomerClient;
import com.lurdharry.ecommerce.exception.BusinessException;
import com.lurdharry.ecommerce.kafka.OrderConfirmation;
import com.lurdharry.ecommerce.kafka.OrderProducer;
import com.lurdharry.ecommerce.orderline.OrderLineRequest;
import com.lurdharry.ecommerce.orderline.OrderLineService;
import com.lurdharry.ecommerce.payment.PaymentClient;
import com.lurdharry.ecommerce.payment.PaymentRequest;
import com.lurdharry.ecommerce.product.ProductClient;
import com.lurdharry.ecommerce.product.PurchaseRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    private final PaymentClient paymentClient;


    public String fallbackMethod(Exception e) {
        return "Fallback response due to: " + e.getMessage();
    }

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

        // start payment process
        var paymentRequest = new PaymentRequest(
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                orderRequest.id(),
                orderRequest.reference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

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

    @CircuitBreaker(name = "order-service", fallbackMethod = "fallbackMethod")
    public List<OrderResponse> findAll() {
        throw new RuntimeException("Simulated failure");
//        return orderRepository.findAll()
//                .stream()
//                .map(mapper::fromOrder)
//                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {

        return orderRepository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(()-> new EntityNotFoundException(String.format("No order found with the provided ID: %d", orderId)));
    }
}
