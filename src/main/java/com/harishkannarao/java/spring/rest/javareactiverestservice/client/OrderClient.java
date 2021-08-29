package com.harishkannarao.java.spring.rest.javareactiverestservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OrderClient {

    private final WebClient webClient;

    @Autowired
    public OrderClient(@Value("${order-service.base-url}") String orderServiceBaseUrl) {
        this.webClient = WebClient.builder().baseUrl(orderServiceBaseUrl).build();
    }
}
