package com.harishkannarao.java.spring.rest.javareactiverestservice.client;

import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderClient {

    private final WebClient webClient;

    @Autowired
    public OrderClient(@Value("${order-service.base-url}") String orderServiceBaseUrl) {
        this.webClient = WebClient.builder().baseUrl(orderServiceBaseUrl).build();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public Flux<Order> getOrders(Optional<UUID> customerId) {
        return webClient.get()
                .uri(uriBuilder -> {
                            UriBuilder builder = uriBuilder
                                    .path("/order");
                            Map<String, String> variables = new HashMap<>();
                            customerId.ifPresent(it -> {
                                builder.queryParam("customer", "{customerId}");
                                variables.put("customerId", it.toString());
                            });
                            return builder.build(variables);
                        }
                )
                .retrieve()
                .bodyToFlux(Order.class);
    }
}
