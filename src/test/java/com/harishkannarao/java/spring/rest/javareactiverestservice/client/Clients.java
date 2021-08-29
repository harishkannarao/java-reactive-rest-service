package com.harishkannarao.java.spring.rest.javareactiverestservice.client;

import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.SpringBootTestRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

public class Clients {

    public static WebTestClient webTestClient() {
        return WebTestClient.bindToServer().baseUrl(SpringBootTestRunner.getApplicationUrl()).build();
    }

    public static CustomerApiClient customerApiClient() {
        return new CustomerApiClient(webTestClient());
    }

    public static CustomerOrderApiClient customerOrderApiClient() {
        return new CustomerOrderApiClient(webTestClient());
    }
}
