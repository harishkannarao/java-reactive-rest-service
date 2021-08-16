package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.assertion.CustomerAssertion;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.response.CreateCustomerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;
import static com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures.randomCustomer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

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


        customerApiClient().deleteAll().expectStatus().isNoContent();

        customerApiClient().getAll().expectBodyList(Customer.class).hasSize(0);
    }

    @Test
    void createMultipleCustomers() {
        customerApiClient().getAll().expectBodyList(Customer.class).hasSize(0);

        Customer input1 = randomCustomer();
        Customer input2 = randomCustomer();
        List<Customer> customerList = List.of(input1, input2);

        customerApiClient().createMultiple(customerList)
                .expectStatus().isOk()
                .expectBodyList(CreateCustomerResponse.class)
                .value(it -> {
                    List<UUID> createdIds = it.stream().map(CreateCustomerResponse::getId).collect(Collectors.toList());
                    assertThat(createdIds).containsExactlyInAnyOrder(input1.getId(), input2.getId());
                });

        customerApiClient().getAll()
                .expectBodyList(Customer.class)
                .value((list) -> {
                    assertThat(list).hasSize(2);
                    Map<UUID, Customer> customerMap = list.stream().collect(Collectors.toMap(Customer::getId, it -> it));
                    CustomerAssertion.assertEquals(customerMap.get(input1.getId()), input1);
                    CustomerAssertion.assertEquals(customerMap.get(input2.getId()), input2);
                });
    }

    @Test
    void returnNotFoundWhenGivenIdDoesNotExist() {
        customerApiClient().get(UUID.randomUUID())
                .expectStatus().isNotFound();
    }

    @Test
    void returnBadRequestWhenGivenIdIsNotValid() {
        customerApiClient().get("junk-uuid")
                .expectStatus().isBadRequest();
    }

    @Test
    void cannotCreateMultipleCustomersWithSameId() {
        Customer input = randomCustomer();

        customerApiClient().create(input)
                .expectStatus().isOk()
                .expectBody(CreateCustomerResponse.class)
                .value((it) -> assertThat(it.getId()).isEqualTo(input.getId()));

        customerApiClient().create(input)
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody().jsonPath("$.message").value(containsString("duplicate key value"));
    }
}
