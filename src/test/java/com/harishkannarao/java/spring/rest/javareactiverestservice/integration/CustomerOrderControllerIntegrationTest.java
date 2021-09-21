package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.OrderFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import com.harishkannarao.java.spring.rest.javareactiverestservice.stub.Stubs;
import org.junit.jupiter.api.Test;
import org.mockserver.model.RequestDefinition;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;
import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerOrderApiClient;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerOrderControllerIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void getCustomerOrders() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        List<Order> orders = List.of(order1, order2);
        Customer customer = CustomerFixtures.randomCustomer();

        Stubs.orderServiceStub()
                .stubGetCustomerOrders(200,
                        customer.id().toString(),
                        Optional.of(jsonUtil().toJson(orders)));

        customerApiClient()
                .create(customer);

        customerOrderApiClient()
                .get(customer.id().toString())
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(2)
                .value(result -> {
                    Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::id, it -> it));
                    assertThat(mappedResult.get(order1.id())).isEqualTo(order1);
                    assertThat(mappedResult.get(order2.id())).isEqualTo(order2);
                });
    }

    @Test
    void getCustomerOrders_returns404_whenCustomerDoNotExist() {
        String customerId = UUID.randomUUID().toString();
        customerOrderApiClient()
                .get(customerId)
                .expectStatus().isNotFound();

        RequestDefinition[] orderRequests = Stubs.orderServiceStub().retrieveGetCustomerOrdersRequests(customerId);
        assertThat(orderRequests).hasSize(0);
    }

    @Test
    void getCustomerOrders_returnsEmpty_on404FromOrderService() {
        Customer customer = CustomerFixtures.randomCustomer();

        Stubs.orderServiceStub()
                .stubGetCustomerOrders(404, customer.id().toString(), Optional.empty());

        customerApiClient()
                .create(customer);

        customerOrderApiClient()
                .get(customer.id().toString())
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(0);

        RequestDefinition[] orderRequests = Stubs.orderServiceStub().retrieveGetCustomerOrdersRequests(customer.id().toString());
        assertThat(orderRequests).hasSize(1);
    }

    @Test
    void getCustomerOrders_returnsError_onErrorFromOrderService() {
        Customer customer = CustomerFixtures.randomCustomer();

        Stubs.orderServiceStub()
                .stubGetCustomerOrders(500, customer.id().toString(), Optional.empty());

        customerApiClient()
                .create(customer);

        customerOrderApiClient()
                .get(customer.id().toString())
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
