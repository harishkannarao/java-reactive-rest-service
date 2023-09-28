package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import org.junit.jupiter.api.Test;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.webTestClient;

class StaticHtmlPageIntTest extends AbstractBaseIntegrationTest {

	@Test
	void verify_static_html_page() {
		webTestClient()
				.get()
				.uri("/html/hello-world.html")
				.exchange()
				.expectStatus().isOk();
	}

}
