package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

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
                    .value((it) -> assertThat(it.id()).isEqualTo(customer.id()));
            customerApiClient().get(customer.id())
                    .expectStatus().isOk()
                    .expectBody(Customer.class)
                    .value((it) -> assertThat(it).isEqualTo(customer));
        });

        customerApiClient().getAll()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .value((it) -> {
                    assertThat(it).hasSize(2);
                    assertThat(it.get(0)).isEqualTo(input1);
                    assertThat(it.get(1)).isEqualTo(input2);
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
                    List<UUID> createdIds = it.stream().map(CreateCustomerResponse::id).collect(Collectors.toList());
                    assertThat(createdIds).containsExactlyInAnyOrder(input1.id(), input2.id());
                });

        customerApiClient().getAll()
                .expectBodyList(Customer.class)
                .value((list) -> {
                    assertThat(list).hasSize(2);
                    Map<UUID, Customer> customerMap = list.stream().collect(Collectors.toMap(Customer::id, it -> it));
                    assertThat(customerMap.get(input1.id())).isEqualTo(input1);
                    assertThat(customerMap.get(input2.id())).isEqualTo(input2);
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
                .value((it) -> assertThat(it.id()).isEqualTo(input.id()));

        customerApiClient().create(input)
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody().jsonPath("$.message").value(containsString("duplicate key value"));
    }
}
