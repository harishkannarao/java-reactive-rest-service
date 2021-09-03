package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.client.OrderClient;
import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.OrderFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import com.harishkannarao.java.spring.rest.javareactiverestservice.stub.Stubs;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.RequestDefinition;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class OrderClientIntegrationTest extends AbstractBaseIntegrationTest {

    private OrderClient underTest() {
        return getBean(OrderClient.class);
    }

    @Test
    void getOrders_withOptionalFields() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        List<Order> orders = List.of(order1, order2);
        Customer customer = CustomerFixtures.randomCustomer();
        Integer limit = 10;

        Stubs.orderServiceStub()
                .stubGetOrders(200,
                        jsonUtil().toJson(orders),
                        Optional.of(customer.getId().toString()),
                        Optional.of(limit));

        List<Order> result = underTest()
                .getOrders(Optional.of(customer.getId()), Optional.of(limit), UUID.randomUUID().toString())
                .collectList().block();

        assertThat(result).hasSize(2);
        Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::getId, it -> it));
        assertThat(mappedResult.get(order1.getId())).usingRecursiveComparison().isEqualTo(order1);
        assertThat(mappedResult.get(order2.getId())).usingRecursiveComparison().isEqualTo(order2);
    }

    @Test
    void getOrders_withOnlyMandatoryFields() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        List<Order> orders = List.of(order1, order2);

        Stubs.orderServiceStub()
                .stubGetOrders(200, jsonUtil().toJson(orders));

        List<Order> result = underTest()
                .getOrders(Optional.empty(), Optional.empty(), UUID.randomUUID().toString())
                .collectList().block();

        assertThat(result).hasSize(2);
        Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::getId, it -> it));
        assertThat(mappedResult.get(order1.getId())).usingRecursiveComparison().isEqualTo(order1);
        assertThat(mappedResult.get(order2.getId())).usingRecursiveComparison().isEqualTo(order2);

        RequestDefinition[] orderRequests = Stubs.orderServiceStub().retrieveGetOrderRequests();
        assertThat(orderRequests).hasSize(1);
        assertThat(((HttpRequest) orderRequests[0]).getQueryStringParameters()).isNull();
    }

    @Test
    void createOrder() {
        Order order = OrderFixtures.randomOrder();

        Stubs.orderServiceStub()
                .stubCreateOrder(204);

        underTest()
                .createOrder(order, UUID.randomUUID().toString())
                .block();

        RequestDefinition[] createOrderRequests = Stubs.orderServiceStub().retrieveCreateOrderRequests();
        assertThat(createOrderRequests).hasSize(1);
        Order receivedOrder = jsonUtil().fromJson(((HttpRequest) createOrderRequests[0]).getBodyAsJsonOrXmlString(), Order.class);
        assertThat(receivedOrder).usingRecursiveComparison().isEqualTo(order);
    }

    @Test
    void deleteOrders() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();

        Stubs.orderServiceStub()
                .stubDeleteOrders(204);

        underTest()
                .deleteOrders(Flux.just(order1, order2), UUID.randomUUID().toString())
                .block();

        RequestDefinition[] deleteOrdersRequests = Stubs.orderServiceStub().retrieveDeleteOrdersRequests();
        assertThat(deleteOrdersRequests).hasSize(1);
        Order[] receivedOrders = jsonUtil().fromJson(((HttpRequest) deleteOrdersRequests[0]).getBodyAsJsonOrXmlString(), Order[].class);
        Map<UUID, Order> mappedReceivedOrders = Arrays.stream(receivedOrders).collect(Collectors.toMap(Order::getId, it -> it));
        assertThat(mappedReceivedOrders.get(order1.getId())).usingRecursiveComparison().isEqualTo(order1);
        assertThat(mappedReceivedOrders.get(order2.getId())).usingRecursiveComparison().isEqualTo(order2);
    }

}
