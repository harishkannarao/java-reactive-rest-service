package com.harishkannarao.java.spring.rest.javareactiverestservice.client;

import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

import java.util.List;
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

    public ResponseSpec create(Customer customer) {
        return webTestClient
                .post()
                .uri("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(customer))
                .exchange();
    }

    public ResponseSpec getAll() {
        return webTestClient
                .get()
                .uri("/customer")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }

    public ResponseSpec deleteAll() {
        return webTestClient
                .delete()
                .uri("/customer")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }

    public ResponseSpec createMultiple(List<Customer> customers) {
        return webTestClient
                .post()
                .uri("/customer/create-multiple")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Flux.fromIterable(customers), Customer.class))
                .exchange();
    }
}
