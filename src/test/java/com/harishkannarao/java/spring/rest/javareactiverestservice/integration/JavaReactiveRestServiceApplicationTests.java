package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

class JavaReactiveRestServiceApplicationTests extends AbstractBaseIntegrationTest {

    @Test
    void contextLoads() {
        webTestClient()
                .get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.error").isEqualTo("Not Found");
    }

}
