package com.harishkannarao.java.spring.rest.javareactiverestservice.client;

import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.UUID;

@Component
public class OrderClient {

    private final WebClient webClient;

    @Autowired
    public OrderClient(@Value("${order-service.base-url}") String orderServiceBaseUrl) {
        this.webClient = WebClient.builder().baseUrl(orderServiceBaseUrl).build();
    }

    public Flux<Order> getCustomerOrders(UUID customerId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/order")
                        .queryParam("customer", "{customerId}")
                        .build(Map.ofEntries(Map.entry("customerId", customerId)))
                )
                .retrieve()
                .bodyToFlux(Order.class);
    }
}
