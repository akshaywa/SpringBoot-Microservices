package com.dailycodebuffer.ProductService.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "my.app")
@Component
@Data
public class AppProperties {
    private String name;
    private String version;
}
