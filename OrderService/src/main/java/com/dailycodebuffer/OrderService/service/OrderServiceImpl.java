package com.dailycodebuffer.OrderService.service;

import com.dailycodebuffer.OrderService.entity.Order;
import com.dailycodebuffer.OrderService.external.client.PaymentService;
import com.dailycodebuffer.OrderService.external.client.ProductService;
import com.dailycodebuffer.OrderService.model.OrderRequest;
import com.dailycodebuffer.OrderService.model.PaymentRequest;
import com.dailycodebuffer.OrderService.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Override
    public String placeOrder(OrderRequest orderRequest) {
        log.info("inside placeOrder method in OrderServiceImpl class. {}", orderRequest.toString());

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("inside placeOrder method in OrderServiceImpl class. PRODUCT-SERVICE called from ORDER-SERVICE");

        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .amount(orderRequest.getAmount())
                .paymentMode(orderRequest.getPaymentMode())
                .build();

        order = orderRepository.save(order);

        log.info("inside placeOrder method in OrderServiceImpl class. Order saved in mongodb. {}", order.toString());

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(order.getPaymentMode())
                .amount(order.getAmount())
                .build();

        String orderStatus = null;
        try{
            paymentService.doPayment(paymentRequest);
            log.info("inside placeOrder method in OrderServiceImpl class. Payment done successfully.");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.info("inside placeOrder method in OrderServiceImpl class. Payment failed.");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("inside placeOrder method in OrderServiceImpl class. Order placed successfully");

        return order.getId();
    }
}
