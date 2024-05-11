package com.dailycodebuffer.OrderService.external.client;

import com.dailycodebuffer.OrderService.model.OrderRequest;
import com.dailycodebuffer.OrderService.model.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("Payment-Service/payment")
public interface PaymentService {
    @PostMapping
    public ResponseEntity<String> doPayment(@RequestBody PaymentRequest paymentRequest);
}
