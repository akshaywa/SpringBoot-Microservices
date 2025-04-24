package com.dailycodebuffer.AuthService.repository;

import com.dailycodebuffer.AuthService.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
