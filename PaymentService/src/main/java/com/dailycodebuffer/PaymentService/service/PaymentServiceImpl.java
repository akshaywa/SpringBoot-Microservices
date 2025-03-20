package com.dailycodebuffer.PaymentService.service;

import com.dailycodebuffer.PaymentService.entity.TransactionDetails;
import com.dailycodebuffer.PaymentService.model.PaymentRequest;
import com.dailycodebuffer.PaymentService.repository.TransactionDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public String doPayment(PaymentRequest paymentRequest) {
        log.info("inside doPayment method in PaymentServiceImpl class. {}", paymentRequest.toString());

        TransactionDetails transactionDetails = TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS").orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .build();

        transactionDetailsRepository.save(transactionDetails);

        log.info("inside doPayment method in PaymentServiceImpl class. Transaction details saved in mongodb. {}", transactionDetails.toString());

        return transactionDetails.getId();
    }
}
