package com.dailycodebuffer.ProductService.service;

import com.dailycodebuffer.ProductService.entity.Product;
import com.dailycodebuffer.ProductService.exception.ProductServiceCustomException;
import com.dailycodebuffer.ProductService.model.ProductRequest;
import com.dailycodebuffer.ProductService.model.ProductResponse;
import com.dailycodebuffer.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public String addProduct(ProductRequest productRequest) {
        log.info("inside addProduct method in ProductServiceImpl class. {}", productRequest.toString());

        Product product = Product.builder().productName(productRequest.getProductName()).quantity(productRequest.getQuantity()).price(productRequest.getPrice()).build();
        product = productRepository.save(product);

        log.info("inside addProduct method in ProductServiceImpl class. Product saved in mongodb. {}", product.toString());

        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(String productId) {
        log.info("inside getProductById method in ProductServiceImpl class. {}", productId);

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductServiceCustomException("Product with given id not found", "PRODUCT_NOT_FOUND"));


        log.info("inside getProductById method in ProductServiceImpl class. Product get from mongodb. {}", productId);

        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);

        return productResponse;
    }
}
