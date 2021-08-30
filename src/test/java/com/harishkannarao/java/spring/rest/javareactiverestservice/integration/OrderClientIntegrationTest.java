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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class OrderClientIntegrationTest extends AbstractBaseIntegrationTest {

    private OrderClient underTest() {
        return getBean(OrderClient.class);
    }

    @Test
    void getOrders_withCustomerId() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        List<Order> orders = List.of(order1, order2);
        Customer customer = CustomerFixtures.randomCustomer();

        Stubs.orderServiceStub()
                .stubOrders(200, jsonUtil().toJson(orders), Optional.of(customer.getId().toString()));

        List<Order> result = underTest().getOrders(Optional.of(customer.getId())).collectList().block();

        assertThat(result).hasSize(2);
        Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::getId, it -> it));
        assertThat(mappedResult.get(order1.getId())).usingRecursiveComparison().isEqualTo(order1);
        assertThat(mappedResult.get(order2.getId())).usingRecursiveComparison().isEqualTo(order2);
    }

    @Test
    void getOrders_withoutCustomerId() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        List<Order> orders = List.of(order1, order2);

        Stubs.orderServiceStub()
                .stubOrders(200, jsonUtil().toJson(orders));

        List<Order> result = underTest().getOrders(Optional.empty()).collectList().block();

        assertThat(result).hasSize(2);
        Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::getId, it -> it));
        assertThat(mappedResult.get(order1.getId())).usingRecursiveComparison().isEqualTo(order1);
        assertThat(mappedResult.get(order2.getId())).usingRecursiveComparison().isEqualTo(order2);

        RequestDefinition[] orderRequests = Stubs.orderServiceStub().getOrderRequests();
        assertThat(orderRequests).hasSize(1);
        assertThat(((HttpRequest) orderRequests[0]).getQueryStringParameters()).isNull();
    }

}
