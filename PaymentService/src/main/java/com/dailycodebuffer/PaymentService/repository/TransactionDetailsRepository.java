package com.dailycodebuffer.PaymentService.repository;

import com.dailycodebuffer.PaymentService.entity.TransactionDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailsRepository extends MongoRepository<TransactionDetails, String> {
}
