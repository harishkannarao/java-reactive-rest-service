package com.harishkannarao.java.spring.rest.javareactiverestservice.client;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

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
}
