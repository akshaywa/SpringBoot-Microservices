package com.dailycodebuffer.ProductService.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("uat")
@Configuration
@Slf4j
public class UatConfig {
    @Autowired
    public UatConfig(AppProperties props) {
        log.info("Name: {}, Version: {}", props.getName(), props.getVersion());
    }
}