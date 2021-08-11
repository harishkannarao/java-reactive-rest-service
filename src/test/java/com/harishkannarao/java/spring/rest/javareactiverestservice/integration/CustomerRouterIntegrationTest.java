package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.assertion.CustomerAssertion;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.response.CreateCustomerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import java.util.List;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;
import static com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures.randomCustomer;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerRouterIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void createReadListAndDeleteCustomer() {
        customerApiClient().getAll().expectBodyList(Customer.class).hasSize(0);

        Customer input1 = randomCustomer();
        Customer input2 = randomCustomer();

        List.of(input1, input2).forEach(customer -> {
            customerApiClient().create(customer)
                    .expectStatus().isOk()
                    .expectBody(CreateCustomerResponse.class)
                    .value((it) -> assertThat(it.getId()).isEqualTo(customer.getId()));
            customerApiClient().get(customer.getId())
                    .expectStatus().isOk()
                    .expectBody(Customer.class)
                    .value((it) -> CustomerAssertion.assertEquals(it, customer));
        });

        customerApiClient().getAll()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .value((it) -> {
                    assertThat(it).hasSize(2);
                    CustomerAssertion.assertEquals(it.get(0), input1);
                    CustomerAssertion.assertEquals(it.get(1), input2);
                });


        customerApiClient().deleteAll().expectStatus().isOk();

        customerApiClient().getAll().expectBodyList(Customer.class).hasSize(0);
    }
}
