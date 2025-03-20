package com.dailycodebuffer.CloudGateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/order")
    public ResponseEntity<String> orderServiceFallback() {
        return new ResponseEntity<>("Order Service is currently unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/product")
    public ResponseEntity<String> productServiceFallback() {
        return new ResponseEntity<>("Product Service is currently unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/payment")
    public ResponseEntity<String> paymentServiceFallback() {
        return new ResponseEntity<>("Payment Service is currently unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }
}