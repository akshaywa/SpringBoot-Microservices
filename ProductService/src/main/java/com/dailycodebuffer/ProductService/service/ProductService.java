package com.dailycodebuffer.ProductService.service;

import com.dailycodebuffer.ProductService.model.ProductRequest;
import com.dailycodebuffer.ProductService.model.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    String addProduct(ProductRequest productRequest);

    ProductResponse getProductById(String productId);

    void reduceQuantity(String productId, long quantity);

    void storeFile(MultipartFile file) throws IOException;
}
