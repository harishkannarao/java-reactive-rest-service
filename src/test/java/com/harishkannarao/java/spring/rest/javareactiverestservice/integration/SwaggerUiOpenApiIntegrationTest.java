package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import org.junit.jupiter.api.Test;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.webTestClient;

public class SwaggerUiOpenApiIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void swaggerUiShouldBeEnabled() {
        webTestClient()
                .get()
                .uri("/swagger-ui.html")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void openApiShouldBeEnabled() {
        webTestClient()
                .get()
                .uri("/api-docs")
                .exchange()
                .expectStatus().isOk();
    }
}
