package com.dailycodebuffer.OrderService.service;

import com.dailycodebuffer.OrderService.entity.Order;
import com.dailycodebuffer.OrderService.external.client.ProductService;
import com.dailycodebuffer.OrderService.model.OrderRequest;
import com.dailycodebuffer.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

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

        return order.getId();
    }
}
