package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
