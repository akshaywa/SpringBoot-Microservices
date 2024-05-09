package com.dailycodebuffer.PaymentService.controller;

import com.dailycodebuffer.PaymentService.model.PaymentRequest;
import com.dailycodebuffer.PaymentService.service.PaymentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Log4j2
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> doPayment(@RequestBody PaymentRequest paymentRequest) {
        log.info("inside doPayment method in PaymentController class. {}", paymentRequest.toString());
        return new ResponseEntity<>(paymentService.doPayment(paymentRequest), HttpStatus.OK);
    }
}
