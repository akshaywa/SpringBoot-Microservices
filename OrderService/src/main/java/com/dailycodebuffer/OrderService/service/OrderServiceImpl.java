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
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    private final SimpMessagingTemplate messagingTemplate;

    public OrderServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public String placeOrder(OrderRequest orderRequest) {
        log.info("inside placeOrder method in OrderServiceImpl class. {}", orderRequest.toString());

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("inside placeOrder method in OrderServiceImpl class. PRODUCT-SERVICE called from ORDER-SERVICE");

        Order order = Order.builder().productId(orderRequest.getProductId()).quantity(orderRequest.getQuantity()).orderDate(Instant.now()).orderStatus("CREATED").amount(orderRequest.getAmount()).paymentMode(orderRequest.getPaymentMode()).build();

        order = orderRepository.save(order);
        String orderMessage = "Order ID: " + order.getId() + " has status: " + order.getOrderStatus();
        messagingTemplate.convertAndSend("/topic/order-updates", orderMessage);


        log.info("inside placeOrder method in OrderServiceImpl class. Order saved in mongodb. {}", order);

        PaymentRequest paymentRequest = PaymentRequest.builder().orderId(order.getId()).paymentMode(order.getPaymentMode()).amount(order.getAmount()).build();

        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("inside placeOrder method in OrderServiceImpl class. Payment done successfully.");
            orderStatus = "PLACED";
            orderMessage = "Order ID: " + order.getId() + " has status: " + orderStatus;
            messagingTemplate.convertAndSend("/topic/order-updates", orderMessage);
        } catch (Exception e) {
            log.info("inside placeOrder method in OrderServiceImpl class. Payment failed.");
            if (!order.getPaymentMode().equals(PaymentMode.CASH)) {
                orderStatus = "PAYMENT_FAILED";
                orderMessage = "Order ID: " + order.getId() + " has status: " + orderStatus;
                messagingTemplate.convertAndSend("/topic/order-updates", orderMessage);
            }
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("inside placeOrder method in OrderServiceImpl class. Order placed successfully");

        return order.getId();
    }
}
