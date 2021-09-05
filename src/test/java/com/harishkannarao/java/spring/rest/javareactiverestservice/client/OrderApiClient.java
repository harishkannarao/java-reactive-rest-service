package com.harishkannarao.java.spring.rest.javareactiverestservice.client;

import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OrderApiClient {
    private final WebTestClient webTestClient;

    public OrderApiClient(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public WebTestClient.ResponseSpec get(Optional<Integer> limit) {
        return webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/order")
                        .queryParamIfPresent("limit", limit)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }

    public WebTestClient.ResponseSpec get() {
        return get(Optional.empty());
    }

    public WebTestClient.ResponseSpec create(Order order) {
        return webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/order").build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(order))
                .exchange();
    }

    public WebTestClient.ResponseSpec create() {
        return webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/order").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }

    public WebTestClient.ResponseSpec delete(List<Order> orders) {
        return webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/order/delete").build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(orders))
                .exchange();
    }
}
