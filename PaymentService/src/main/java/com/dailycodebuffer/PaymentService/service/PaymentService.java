package com.dailycodebuffer.PaymentService.service;

import com.dailycodebuffer.PaymentService.model.PaymentRequest;

public interface PaymentService {
    String doPayment(PaymentRequest paymentRequest);
}
