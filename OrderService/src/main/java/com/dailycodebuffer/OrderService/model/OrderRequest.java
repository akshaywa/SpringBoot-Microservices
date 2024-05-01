package com.dailycodebuffer.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private String productId;
    private long quantity;
    private long amount;
    private PaymentMode paymentMode;
}
