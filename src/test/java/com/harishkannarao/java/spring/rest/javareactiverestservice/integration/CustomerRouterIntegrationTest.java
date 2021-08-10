package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.assertion.CustomerAssertion;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures.randomCustomer;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class CustomerRouterIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void getCustomerById() {
        Customer input = randomCustomer();
        CustomerRepository customerRepository = getBean(CustomerRepository.class);
        customerRepository.createCustomer(input).block();

        EntityExchangeResult<Customer> response = webTestClient()
                .get()
                .uri("/customer/{id}", input.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(Customer.class)
                .returnResult();

        assertThat(response.getRawStatusCode()).isEqualTo(200);

        Customer result = response.getResponseBody();
        CustomerAssertion.assertEquals(result, input);
    }
}
