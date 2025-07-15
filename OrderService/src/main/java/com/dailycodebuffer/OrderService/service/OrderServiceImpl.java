package com.dailycodebuffer.OrderService.service;

import com.dailycodebuffer.OrderService.entity.Order;
import com.dailycodebuffer.OrderService.external.client.PaymentService;
import com.dailycodebuffer.OrderService.external.client.ProductService;
import com.dailycodebuffer.OrderService.model.OrderRequest;
import com.dailycodebuffer.OrderService.model.PaymentMode;
import com.dailycodebuffer.OrderService.model.PaymentRequest;
import com.dailycodebuffer.OrderService.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    private final SimpMessagingTemplate messagingTemplate;

    public OrderServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public String placeOrder(OrderRequest orderRequest) {
        log.info("inside placeOrder method in OrderServiceImpl class. {}", orderRequest.toString());

        Order order = Order.builder().productId(orderRequest.getProductId()).quantity(orderRequest.getQuantity()).orderDate(Instant.now()).orderStatus("CREATED").amount(orderRequest.getAmount()).paymentMode(orderRequest.getPaymentMode()).build();

        order = orderRepository.save(order);
        String orderMessage = "Order ID: " + order.getId() + " has status: " + order.getOrderStatus();
        messagingTemplate.convertAndSend("/topic/order-updates", orderMessage);

        PaymentRequest paymentRequest = PaymentRequest.builder().orderId(order.getId()).paymentMode(order.getPaymentMode()).amount(order.getAmount()).build();

        CompletableFuture<String> productFuture = callProductService(orderRequest.getProductId(), orderRequest.getQuantity());
        CompletableFuture<String> paymentFuture = callPaymentService(paymentRequest);

        CompletableFuture.allOf(productFuture, paymentFuture).join();

        String productResult = productFuture.join();
        String paymentResult = paymentFuture.join();

        String orderStatus;
        if (productResult.startsWith("Product quantity reduced") && !paymentResult.startsWith("Payment failed")) {
            orderStatus = "PLACED";
            orderMessage = "Order ID: " + order.getId() + " has status: " + orderStatus;
            messagingTemplate.convertAndSend("/topic/order-updates", orderMessage);
            log.info("inside placeOrder method in OrderServiceImpl class. Payment done successfully.");
        } else {
            if (!order.getPaymentMode().equals(PaymentMode.CASH)) {
                orderStatus = "PAYMENT_FAILED";
                orderMessage = "Order ID: " + order.getId() + " has status: " + orderStatus;
                messagingTemplate.convertAndSend("/topic/order-updates", orderMessage);
                log.info("inside placeOrder method in OrderServiceImpl class. Payment failed.");
            } else {
                orderStatus = "FAILED";
            }
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("inside placeOrder method in OrderServiceImpl class. Order placed successfully");

        return order.getId();
    }


    @Async
    public CompletableFuture<String> callPaymentService(PaymentRequest paymentRequest) {
        try {
            ResponseEntity<String> response = paymentService.doPayment(paymentRequest);
            return CompletableFuture.completedFuture(response.getBody());
        } catch (Exception e) {
            return CompletableFuture.completedFuture("Payment failed: " + e.getMessage());
        }
    }


    @Async
    public CompletableFuture<String> callProductService(String productId, long quantity) {
        try {
            productService.reduceQuantity(productId, quantity);
            return CompletableFuture.completedFuture("Product quantity reduced for " + productId);
        } catch (Exception e) {
            return CompletableFuture.completedFuture("Product quantity reduce failed: " + e.getMessage());
        }
    }

}
