package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.assertion.CustomerAssertion;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.response.CreateCustomerResponse;
import com.harishkannarao.java.spring.rest.javareactiverestservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import java.util.List;
import java.util.UUID;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;
import static com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures.randomCustomer;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class CustomerRouterIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void createReadListAndDeleteCustomer() {
        customerApiClient().getAll().expectBodyList(Customer.class).hasSize(0);

        Customer input1 = randomCustomer();
        Customer input2 = randomCustomer();
        List.of(input1, input2).forEach(customer -> {
            ResponseSpec createRes = customerApiClient().create(customer);
            createRes.expectStatus().isOk();
            UUID createdId = createRes.expectBody(CreateCustomerResponse.class).returnResult().getResponseBody().getId();
            assertThat(createdId).isEqualTo(customer.getId());
            ResponseSpec getResponse = customerApiClient().get(customer.getId());
            getResponse.expectStatus().isOk();
            CustomerAssertion.assertEquals(getResponse.expectBody(Customer.class).returnResult().getResponseBody(), customer);
        });

        ResponseSpec getAllRes = customerApiClient().getAll();
        getAllRes.expectStatus().isOk();

        List<Customer> getAllResult = getAllRes.expectBodyList(Customer.class).returnResult().getResponseBody();
        assertThat(getAllResult).hasSize(2);
        CustomerAssertion.assertEquals(getAllResult.get(0), input1);
        CustomerAssertion.assertEquals(getAllResult.get(1), input2);

        ResponseSpec deleteRes = customerApiClient().deleteAll();
        deleteRes.expectStatus().isOk();

        customerApiClient().getAll().expectBodyList(Customer.class).hasSize(0);
    }
}
