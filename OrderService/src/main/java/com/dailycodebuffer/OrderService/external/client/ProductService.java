package com.dailycodebuffer.OrderService.external.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("Product-Service/product")
public interface ProductService {
    @CircuitBreaker(name = "productServiceCircuitBreaker", fallbackMethod = "fallbackReduceQuantity")
    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable("id") String productId, @RequestParam long quantity);

    default ResponseEntity<Void> fallbackReduceQuantity(Throwable throwable) {
        System.out.println("Fallback triggered for reduceQuantity: " + throwable.getMessage());
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
