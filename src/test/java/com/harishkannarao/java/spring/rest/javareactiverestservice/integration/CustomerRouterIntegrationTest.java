package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.assertion.CustomerAssertion;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;
import static com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures.randomCustomer;

@SuppressWarnings("ConstantConditions")
public class CustomerRouterIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void getCustomerById() {
        Customer input = randomCustomer();
        CustomerRepository customerRepository = getBean(CustomerRepository.class);
        customerRepository.createCustomer(input).block();

        ResponseSpec response = customerApiClient().get(input.getId());

        response.expectStatus().isOk();

        Customer result = response.expectBody(Customer.class).returnResult().getResponseBody();
        CustomerAssertion.assertEquals(result, input);
    }
}
