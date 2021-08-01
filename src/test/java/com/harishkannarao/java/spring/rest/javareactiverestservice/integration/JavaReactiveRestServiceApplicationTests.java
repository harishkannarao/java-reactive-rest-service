package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class JavaReactiveRestServiceApplicationTests extends AbstractBaseIntegrationTest {

	@Test
	void contextLoads() {
		webTestClient()
				.get()
				.uri("/")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound();
	}

}
