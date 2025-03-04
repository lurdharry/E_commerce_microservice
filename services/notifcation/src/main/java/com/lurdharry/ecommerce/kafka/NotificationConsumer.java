package com.lurdharry.ecommerce.kafka;

import com.lurdharry.ecommerce.email.EmailService;
import com.lurdharry.ecommerce.kafka.order.OrderConformation;
import com.lurdharry.ecommerce.kafka.payment.PaymentConformation;
import com.lurdharry.ecommerce.notification.Notification;
import com.lurdharry.ecommerce.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.lurdharry.ecommerce.notification.NotificationType.ORDER_CONFIRMATION;
import static com.lurdharry.ecommerce.notification.NotificationType.PAYMENT_CONFORMATION;
import static java.lang.String.format;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationRepository repository;
    private final EmailService emailService;


    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConformation paymentConformation) throws MessagingException {
        log.info(format("Consuming the message from payment-topic Topic:: %s", paymentConformation));

        repository.save(
                Notification.builder()
                        .type(PAYMENT_CONFORMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConformation(paymentConformation)
                        .build()
        );

        // send email
        var customerName = paymentConformation.customerFirstname() + " " + paymentConformation.customerLastname();
        emailService.sendPaymentSuccessEmail(
                paymentConformation.customerEmail(), 
                customerName,
                paymentConformation.amount(),
                paymentConformation.oderReference()
        );
    }


    @KafkaListener(topics = "order-topic")
    public void consumePaymentSuccessNotification(OrderConformation orderConformation) throws MessagingException {
        log.info(format("Consuming the message from order-topic Topic:: %s", orderConformation));

        repository.save(
                Notification.builder()
                        .type(ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConformation( orderConformation)
                        .build()
        );

        // send email
        var customerName = orderConformation.customer().firstname() + " " + orderConformation.customer().lastname();
        emailService.sendOrderConfirmationEmail(
                orderConformation.customer().email(),
                customerName,
                orderConformation.totalAmount(),
                orderConformation.orderReference(),
                orderConformation.products()
        );
    }

}
