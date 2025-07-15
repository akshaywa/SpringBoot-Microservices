package com.dailycodebuffer.ProductService.controller;

import com.dailycodebuffer.ProductService.model.ProductRequest;
import com.dailycodebuffer.ProductService.model.ProductResponse;
import com.dailycodebuffer.ProductService.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest productRequest) {
        log.debug("inside addProduct method in ProductController class. {}", productRequest.toString());
        String productId = productService.addProduct(productRequest);
        return new ResponseEntity<>(productId, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") String productId) {
        log.debug("inside getProductById method in ProductController class. {}", productId);
        ProductResponse productResponse = productService.getProductById(productId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PatchMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable("id") String productId, @RequestParam long quantity) {
        log.debug("inside reduceQuantity method in ProductController class. {}", productId);
        productService.reduceQuantity(productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        log.debug("inside uploadFile method in ProductController class. {}", file.getOriginalFilename());
        try {
            productService.storeFile(file);
            return new ResponseEntity<>("File uploaded and stored successfully.", HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error processing file: {}", e.getMessage());
            return new ResponseEntity<>("Error processing file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}