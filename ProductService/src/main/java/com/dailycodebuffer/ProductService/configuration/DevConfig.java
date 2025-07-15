package com.dailycodebuffer.ProductService.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
@Slf4j
public class DevConfig {
    @Autowired
    public DevConfig(AppProperties props) {
        log.info("Name: {}, Version: {}", props.getName(), props.getVersion());
    }
}
