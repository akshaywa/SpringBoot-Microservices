package com.dailycodebuffer.ProductService.controller;

import com.dailycodebuffer.ProductService.model.ProductRequest;
import com.dailycodebuffer.ProductService.model.ProductResponse;
import com.dailycodebuffer.ProductService.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RefreshScope
@RequestMapping("/product")
@Slf4j
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest productRequest) {
        log.debug("Inside addProduct method - Request: {}", productRequest);
        String productId = productService.addProduct(productRequest);
        log.info("Product added successfully - ID: {}", productId);
        return new ResponseEntity<>(productId, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") String productId) {
        log.debug("Inside getProductById method - Product ID: {}", productId);
        ProductResponse productResponse = productService.getProductById(productId);
        log.info("Product retrieved successfully - Product: {}", productResponse);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PatchMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable("id") String productId, @RequestParam long quantity) {
        log.debug("Inside reduceQuantity method - Product ID: {}, Quantity: {}", productId, quantity);
        productService.reduceQuantity(productId, quantity);
        log.info("Product quantity reduced successfully - Product ID: {}, New Quantity: {}", productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        log.debug("Inside uploadFile method - File: {}", file.getOriginalFilename());
        try {
            productService.storeFile(file);
            log.info("File uploaded and stored successfully - File: {}", file.getOriginalFilename());
            return new ResponseEntity<>("File uploaded and stored successfully.", HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error processing file: {}", e.getMessage());
            return new ResponseEntity<>("Error processing file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}