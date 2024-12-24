package com.remit.recover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.remit.recover.config.SecurityWhiteListConfig;

@SpringBootApplication
@EnableConfigurationProperties(SecurityWhiteListConfig.class)
public class DRGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(DRGatewayApplication.class, args);
    }
}

