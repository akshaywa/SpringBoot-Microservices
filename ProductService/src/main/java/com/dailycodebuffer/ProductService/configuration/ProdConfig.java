package com.dailycodebuffer.ProductService.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
@Slf4j
public class ProdConfig {
    @Autowired
    public ProdConfig(AppProperties props) {
        log.info("Name: {}, Version: {}", props.getName(), props.getVersion());
    }
}
