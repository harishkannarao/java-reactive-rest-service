package com.harishkannarao.java.spring.rest.javareactiverestservice.client;

import com.harishkannarao.java.spring.rest.javareactiverestservice.filter.WebClientLoggingFilter;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.filter.WebClientLoggingFilter.REQUEST_ID;

@Component
public class OrderClient {

    private final WebClient webClient;

    @Autowired
    public OrderClient(
            WebClient.Builder webClientBuilder,
            WebClientLoggingFilter webClientLoggingFilter,
            @Value("${order-service.base-url}") String orderServiceBaseUrl) {
        this.webClient = webClientBuilder
                .baseUrl(orderServiceBaseUrl)
                .filter(webClientLoggingFilter)
                .build();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public Flux<Order> getOrders(Optional<UUID> customerId, Optional<Integer> limit, String requestId) {
        return webClient.get()
                .uri(uriBuilder -> {
                            UriBuilder builder = uriBuilder
                                    .path("/order");
                            Map<String, String> variables = new HashMap<>();
                            customerId.ifPresent(it -> {
                                builder.queryParam("customer", "{customerId}");
                                variables.put("customerId", it.toString());
                            });
                            limit.ifPresent(it -> {
                                builder.queryParam("limit", "{limit}");
                                variables.put("limit", it.toString());
                            });
                            return builder.build(variables);
                        }
                )
                .attribute(REQUEST_ID, requestId)
                .retrieve()
                .bodyToFlux(Order.class);
    }

    public Mono<Void> createOrder(Mono<Order> order, String requestId) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/order").build())
                .attribute(REQUEST_ID, requestId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromProducer(order, Order.class))
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> deleteOrders(Flux<Order> orders, String requestId) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/order/delete").build())
                .attribute(REQUEST_ID, requestId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromProducer(orders, Order.class))
                .retrieve()
                .bodyToMono(Void.class);
    }
}
