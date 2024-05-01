package com.dailycodebuffer.OrderService.service;

import com.dailycodebuffer.OrderService.model.OrderRequest;

public interface OrderService {
    String placeOrder(OrderRequest orderRequest);
}
