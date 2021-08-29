package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.client.OrderClient;
import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.OrderFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import com.harishkannarao.java.spring.rest.javareactiverestservice.stub.Stubs;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class OrderClientIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void getCustomerOrders() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        Customer customer = CustomerFixtures.randomCustomer();
        List<Order> orders = List.of(order1, order2);

        Stubs.orderServiceStub()
                .stubCustomerOrders(200, jsonUtil().toJson(orders), customer.getId().toString());

        OrderClient underTest = getBean(OrderClient.class);
        List<Order> result = underTest.getCustomerOrders(customer.getId()).collectList().block();

        assertThat(result).hasSize(2);
        Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::getId, it -> it));
        assertThat(mappedResult.get(order1.getId())).usingRecursiveComparison().isEqualTo(order1);
        assertThat(mappedResult.get(order2.getId())).usingRecursiveComparison().isEqualTo(order2);
    }

}
