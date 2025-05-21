package com.dailycodebuffer.ProductService.service;

import com.dailycodebuffer.ProductService.entity.Product;
import com.dailycodebuffer.ProductService.entity.UploadedFile;
import com.dailycodebuffer.ProductService.exception.ProductServiceCustomException;
import com.dailycodebuffer.ProductService.model.ProductRequest;
import com.dailycodebuffer.ProductService.model.ProductResponse;
import com.dailycodebuffer.ProductService.repository.ProductRepository;
import com.dailycodebuffer.ProductService.repository.UploadedFileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @Override
    public String addProduct(ProductRequest productRequest) {
        log.info("inside addProduct method in ProductServiceImpl class. {}", productRequest.toString());

        Product product = Product.builder().productName(productRequest.getProductName()).quantity(productRequest.getQuantity()).price(productRequest.getPrice()).build();
        product = productRepository.save(product);

        log.info("inside addProduct method in ProductServiceImpl class. Product saved in mongodb. {}", product);

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

    @Override
    public void reduceQuantity(String productId, long quantity) {
        log.info("inside reduceQuantity method in ProductServiceImpl class. {} {}", productId, quantity);

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductServiceCustomException("Product with given id not found", "PRODUCT_NOT_FOUND"));

        if (product.getQuantity() < quantity) {
            throw new ProductServiceCustomException("Product with given id does not have sufficient quantity", "INSUFFICIENT_QUANTITY");
        }

        product.setQuantity(product.getQuantity() - quantity);
        product = productRepository.save(product);

        log.info("inside reduceQuantity method in ProductServiceImpl class. Product after order completed {} {}", product.getProductId(), product.getQuantity());
    }


    @Override
    public void storeFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);

        UploadedFile document = new UploadedFile();
        document.setFilename(filename);
        document.setUploadTime(LocalDateTime.now());

        assert filename != null;
        if (filename.endsWith(".json")) {
            document.setContentType("json");
            document.setContent(objectMapper.readValue(content, Map.class));
        } else if (filename.endsWith(".yaml") || filename.endsWith(".yml")) {
            document.setContentType("yaml");
            Yaml yaml = new Yaml();
            Map<String, Object> parsed = yaml.load(content);
            document.setContent(parsed);
        } else {
            document.setContentType("text");
            document.setRaw(content);
        }

        uploadedFileRepository.save(document);

    }

}
