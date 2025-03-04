package com.lurdharry.ecommerce.notification;

import com.lurdharry.ecommerce.kafka.order.OrderConformation;
import com.lurdharry.ecommerce.kafka.payment.PaymentConformation;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {

    @Id
    private String id;

    private NotificationType type;

    private LocalDateTime notificationDate;

    private OrderConformation orderConformation;

    private PaymentConformation paymentConformation;
}
