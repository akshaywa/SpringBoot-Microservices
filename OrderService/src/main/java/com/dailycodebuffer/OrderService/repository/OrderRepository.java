package com.dailycodebuffer.OrderService.repository;

import com.dailycodebuffer.OrderService.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}
