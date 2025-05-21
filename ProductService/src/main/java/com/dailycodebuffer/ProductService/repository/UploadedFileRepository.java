package com.dailycodebuffer.ProductService.repository;

import com.dailycodebuffer.ProductService.entity.UploadedFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadedFileRepository extends MongoRepository<UploadedFile, String> {
}

