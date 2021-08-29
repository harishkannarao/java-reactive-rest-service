package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.OrderFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import com.harishkannarao.java.spring.rest.javareactiverestservice.stub.Stubs;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.RequestDefinition;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;
import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerOrderApiClient;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderControllerIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void getCustomerOrders() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        List<Order> orders = List.of(order1, order2);
        Customer customer = CustomerFixtures.randomCustomer();

        Stubs.orderServiceStub()
                .stubOrders(200, jsonUtil().toJson(orders));

        customerApiClient()
                .create(customer);

        customerOrderApiClient()
                .get(customer.getId().toString())
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(2)
                .value(result -> {
                    Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::getId, it -> it));
                    assertThat(mappedResult.get(order1.getId())).usingRecursiveComparison().isEqualTo(order1);
                    assertThat(mappedResult.get(order2.getId())).usingRecursiveComparison().isEqualTo(order2);
                });

        RequestDefinition[] orderRequests = Stubs.orderServiceStub().getOrderRequests();
        assertThat(orderRequests).hasSize(1);
        assertThat(((HttpRequest) orderRequests[0]).getFirstQueryStringParameter("customer"))
                .contains(customer.getId().toString());
    }

    @Test
    void getCustomerOrders_returns404_whenCustomerDoNotExist() {
        customerOrderApiClient()
                .get(UUID.randomUUID().toString())
                .expectStatus().isNotFound();

        RequestDefinition[] orderRequests = Stubs.orderServiceStub().getOrderRequests();
        assertThat(orderRequests).hasSize(0);
    }
}
