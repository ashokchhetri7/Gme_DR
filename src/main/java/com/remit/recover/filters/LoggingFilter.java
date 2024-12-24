// src/main/java/com/remit/recover/filters/LoggingFilter.java

package com.remit.recover.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class LoggingFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        logger.info("Incoming request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
        return chain.filter(exchange).doOnSuccess(aVoid -> {
            logger.info("Outgoing response: {}", exchange.getResponse().getStatusCode());
            logger.debug("Unauthorized access attempt for path: {}", exchange.getRequest().getPath());
        });
    }
}
