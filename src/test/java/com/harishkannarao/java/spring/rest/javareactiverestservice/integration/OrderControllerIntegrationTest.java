package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.client.OrderApiClient;
import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.OrderFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import com.harishkannarao.java.spring.rest.javareactiverestservice.stub.Stubs;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.RequestDefinition;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.orderApiClient;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderControllerIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void getOrders() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        List<Order> orders = List.of(order1, order2);

        Stubs.orderServiceStub()
                .stubGetOrders(200, jsonUtil().toJson(orders), Optional.empty(), Optional.empty());

        orderApiClient()
                .get()
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(2)
                .value(result -> {
                    Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::getId, it -> it));
                    assertThat(mappedResult.get(order1.getId())).usingRecursiveComparison().isEqualTo(order1);
                    assertThat(mappedResult.get(order2.getId())).usingRecursiveComparison().isEqualTo(order2);
                });
    }

    @Test
    void getOrders_withLimit() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        List<Order> orders = List.of(order1, order2);

        Stubs.orderServiceStub()
                .stubGetOrders(200, jsonUtil().toJson(orders), Optional.empty(), Optional.of(10));

        orderApiClient()
                .get(Optional.of(10))
                .expectStatus().isOk()
                .expectBodyList(Order.class)
                .hasSize(2)
                .value(result -> {
                    Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::getId, it -> it));
                    assertThat(mappedResult.get(order1.getId())).usingRecursiveComparison().isEqualTo(order1);
                    assertThat(mappedResult.get(order2.getId())).usingRecursiveComparison().isEqualTo(order2);
                });
    }

    @Test
    void createOrder() {
        Order order = OrderFixtures.randomOrder();

        Stubs.orderServiceStub()
                .stubCreateOrder(204);

        orderApiClient()
                .create(order)
                .expectStatus().isNoContent();

        RequestDefinition[] createOrderRequests = Stubs.orderServiceStub().retrieveCreateOrderRequests();
        assertThat(createOrderRequests).hasSize(1);
        Order receivedOrder = jsonUtil().fromJson(((HttpRequest) createOrderRequests[0]).getBodyAsJsonOrXmlString(), Order.class);
        assertThat(receivedOrder).usingRecursiveComparison().isEqualTo(order);
    }
}
