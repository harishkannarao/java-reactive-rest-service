package com.harishkannarao.java.spring.rest.javareactiverestservice.client;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class CustomerOrderApiClient {
    private final WebTestClient webTestClient;

    public CustomerOrderApiClient(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public WebTestClient.ResponseSpec get(String id) {
        return webTestClient
                .get()
                .uri("/customer/{id}/order", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }
}
