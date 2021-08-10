package com.harishkannarao.java.spring.rest.javareactiverestservice.client;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import java.util.UUID;

public class CustomerApiClient {
    private final WebTestClient webTestClient;

    public CustomerApiClient(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public ResponseSpec get(UUID id) {
        return webTestClient
                .get()
                .uri("/customer/{id}", id.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }
}
