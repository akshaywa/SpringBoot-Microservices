package com.dailycodebuffer.OrderService.entity;

import com.dailycodebuffer.OrderService.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection="order_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    private String id;
    private String productId;
    private long quantity;
    private Instant orderDate;
    private String orderStatus;
    private PaymentMode paymentMode;
    private long amount;
}
