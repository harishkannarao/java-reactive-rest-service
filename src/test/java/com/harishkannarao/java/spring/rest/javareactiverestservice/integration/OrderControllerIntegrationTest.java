package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.OrderFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import com.harishkannarao.java.spring.rest.javareactiverestservice.stub.Stubs;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.RequestDefinition;

import java.util.*;
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
                    Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::id, it -> it));
                    assertThat(mappedResult.get(order1.id())).isEqualTo(order1);
                    assertThat(mappedResult.get(order2.id())).isEqualTo(order2);
                });
    }

    @Test
    void getOrders_withLimit() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        List<Order> orders = List.of(order1, order2);

        Stubs.orderServiceStub()
                .stubGetOrders(200, jsonUtil().toJson(orders), Optional.of(10), Optional.empty());

        orderApiClient()
                .get(Optional.of(10))
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
        assertThat(receivedOrder).isEqualTo(order);
    }

    @Test
    void createOrder_withEmptyBody_doesNotCallOrderServiceApi() {
        orderApiClient()
                .create()
                .expectStatus().isNoContent();

        RequestDefinition[] createOrderRequests = Stubs.orderServiceStub().retrieveCreateOrderRequests();
        assertThat(createOrderRequests).hasSize(0);
    }

    @Test
    void deleteOrders() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        Order order3 = OrderFixtures.randomOrder();
        Order order4 = OrderFixtures.randomOrder();
        Order order5 = OrderFixtures.randomOrder();

        Stubs.orderServiceStub()
                .stubDeleteOrders(204);

        orderApiClient()
                .delete(List.of(order1, order2, order3, order4, order5))
                .expectStatus().isNoContent();

        RequestDefinition[] deleteOrdersRequests = Stubs.orderServiceStub().retrieveDeleteOrdersRequests();
        assertThat(deleteOrdersRequests).hasSize(1);
        Order[] receivedOrders = jsonUtil().fromJson(((HttpRequest) deleteOrdersRequests[0]).getBodyAsJsonOrXmlString(), Order[].class);
        Map<UUID, Order> mappedReceivedOrders = Arrays.stream(receivedOrders).collect(Collectors.toMap(Order::id, it -> it));
        assertThat(mappedReceivedOrders.get(order1.id())).isEqualTo(order1);
        assertThat(mappedReceivedOrders.get(order2.id())).isEqualTo(order2);
        assertThat(mappedReceivedOrders.get(order3.id())).isEqualTo(order3);
        assertThat(mappedReceivedOrders.get(order4.id())).isEqualTo(order4);
        assertThat(mappedReceivedOrders.get(order5.id())).isEqualTo(order5);
    }

    @Test
    void deleteOrders_onMissingBody_doesNothing() {
        Stubs.orderServiceStub()
                .stubDeleteOrders(204);

        orderApiClient()
                .delete(true)
                .expectStatus().isNoContent();

        RequestDefinition[] deleteOrdersRequests = Stubs.orderServiceStub().retrieveDeleteOrdersRequests();
        assertThat(deleteOrdersRequests).hasSize(1);
        Order[] receivedOrders = jsonUtil().fromJson(((HttpRequest) deleteOrdersRequests[0]).getBodyAsJsonOrXmlString(), Order[].class);
        assertThat(receivedOrders).hasSize(0);
    }

    @Test
    void deleteOrders_returnsError_onMissingMandatoryHeader() {
        orderApiClient()
                .delete(false)
                .expectStatus().isBadRequest();
    }

    @Test
    void deleteOrders_returnsError_onOrdersMoreThan5() {
        orderApiClient()
                .delete(List.of(
                        OrderFixtures.randomOrder(),
                        OrderFixtures.randomOrder(),
                        OrderFixtures.randomOrder(),
                        OrderFixtures.randomOrder(),
                        OrderFixtures.randomOrder(),
                        OrderFixtures.randomOrder()
                ))
                .expectStatus().isBadRequest();

        RequestDefinition[] deleteOrdersRequests = Stubs.orderServiceStub().retrieveDeleteOrdersRequests();
        assertThat(deleteOrdersRequests).hasSize(0);
    }
}
