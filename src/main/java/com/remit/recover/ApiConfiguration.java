package com.remit.recover;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ApiConfiguration implements WebMvcConfigurer {
    public static final Set<String> ALLOWED_ENDPOINTS = new HashSet<>();

    static {
        ALLOWED_ENDPOINTS.add("/api/v3/mobile/CustomerProfile");
        ALLOWED_ENDPOINTS.add("/api/v1/users/access-code");
        ALLOWED_ENDPOINTS.add("/api/v1/mobile/ConfirmPassword");
        ALLOWED_ENDPOINTS.add("/api/v1/mobile/Zero/ping");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
    }
}