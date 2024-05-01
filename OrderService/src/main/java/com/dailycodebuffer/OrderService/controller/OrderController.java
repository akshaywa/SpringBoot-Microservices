package com.dailycodebuffer.OrderService.controller;

import com.dailycodebuffer.OrderService.model.OrderRequest;
import com.dailycodebuffer.OrderService.service.OrderService;
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
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder (@RequestBody OrderRequest orderRequest) {
        log.info("inside placeOrder method in OrderController class. {}",orderRequest);
        String orderId = orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
}