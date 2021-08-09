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
public class CustomerControllerIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void listCustomers() {
        Customer input1 = randomCustomer();
        Customer input2 = randomCustomer();
        CustomerRepository underTest = getBean(CustomerRepository.class);
        underTest.deleteAllCustomers().block();
        underTest.createCustomer(input1).block();
        underTest.createCustomer(input2).block();

        EntityExchangeResult<List<Customer>> response = webTestClient()
                .get()
                .uri("/customer")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBodyList(Customer.class)
                .returnResult();

        assertThat(response.getRawStatusCode()).isEqualTo(200);

        List<Customer> result = response.getResponseBody();
        assertThat(result).hasSize(2);
        CustomerAssertion.assertEquals(result.get(0), input1);
        CustomerAssertion.assertEquals(result.get(1), input2);
    }
}
