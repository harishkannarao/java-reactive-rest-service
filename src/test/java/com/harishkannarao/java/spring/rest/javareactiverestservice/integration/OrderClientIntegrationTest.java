package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.client.OrderClient;
import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.OrderFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import com.harishkannarao.java.spring.rest.javareactiverestservice.stub.Stubs;
import io.netty.handler.timeout.ReadTimeoutException;
import org.junit.jupiter.api.Test;
import org.mockserver.model.Delay;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.RequestDefinition;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

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
        Integer limit = 10;

        Stubs.orderServiceStub()
                .stubGetOrders(200,
                        jsonUtil().toJson(orders),
                        Optional.of(limit),
                        Optional.empty());

        List<Order> result = underTest()
                .getOrders(Optional.of(limit), UUID.randomUUID().toString())
                .collectList().block();

        assertThat(result).hasSize(2);
        Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::id, it -> it));
        assertThat(mappedResult.get(order1.id())).isEqualTo(order1);
        assertThat(mappedResult.get(order2.id())).isEqualTo(order2);
    }

    @Test
    void getOrders_withOnlyMandatoryFields() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        List<Order> orders = List.of(order1, order2);

        Stubs.orderServiceStub()
                .stubGetOrders(200, jsonUtil().toJson(orders));

        List<Order> result = underTest()
                .getOrders( Optional.empty(), UUID.randomUUID().toString())
                .collectList().block();

        assertThat(result).hasSize(2);
        Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::id, it -> it));
        assertThat(mappedResult.get(order1.id())).isEqualTo(order1);
        assertThat(mappedResult.get(order2.id())).isEqualTo(order2);

        RequestDefinition[] orderRequests = Stubs.orderServiceStub().retrieveGetOrderRequests();
        assertThat(orderRequests).hasSize(1);
        assertThat(((HttpRequest) orderRequests[0]).getQueryStringParameters()).isNull();
    }

    @Test
    void getOrders_shouldTimeout() {
        Stubs.orderServiceStub()
                .stubGetOrders(200, "[]", Optional.empty(), Optional.of(Delay.seconds(2)));

        WebClientRequestException result = catchThrowableOfType(() -> underTest()
                .getOrders(Optional.empty(), UUID.randomUUID().toString())
                .collectList().block(), WebClientRequestException.class);

        assertThat(result.getCause()).isInstanceOf(ReadTimeoutException.class);
    }

    @Test
    void getOrder_byId() {
        Order order = OrderFixtures.randomOrder();
        Stubs.orderServiceStub()
                .stubGetOrder(order.id(), 200, jsonUtil().toJson(order));

        Order result = underTest()
                .getOrder(order.id(), UUID.randomUUID().toString())
                .block();

        assertThat(result).isEqualTo(order);
    }

    @Test
    void getOrder_byId_returnsEmptyMono() {
        Order order = OrderFixtures.randomOrder();
        Stubs.orderServiceStub()
                .stubGetOrder(order.id(), 404, "");

        Optional<Order> result = underTest()
                .getOrder(order.id(), UUID.randomUUID().toString())
                .blockOptional();

        assertThat(result).isEmpty();
    }

    @Test
    void getCustomerOrders() {
        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        Customer customer = CustomerFixtures.randomCustomer();
        List<Order> orders = List.of(order1, order2);

        Stubs.orderServiceStub()
                .stubGetCustomerOrders(200, customer.id().toString(), Optional.of(jsonUtil().toJson(orders)));

        List<Order> result = underTest()
                .getCustomerOrders(customer.id(), UUID.randomUUID().toString())
                .collectList().block();

        assertThat(result).hasSize(2);
        Map<UUID, Order> mappedResult = result.stream().collect(Collectors.toMap(Order::id, it -> it));
        assertThat(mappedResult.get(order1.id())).isEqualTo(order1);
        assertThat(mappedResult.get(order2.id())).isEqualTo(order2);
    }

    @Test
    void getCustomerOrders_returnsEmptyOn404() {
        Customer customer = CustomerFixtures.randomCustomer();

        Stubs.orderServiceStub()
                .stubGetCustomerOrders(404, customer.id().toString(), Optional.empty());

        List<Order> result = underTest()
                .getCustomerOrders(customer.id(), UUID.randomUUID().toString())
                .collectList().block();

        assertThat(result).isEmpty();
    }

    @Test
    void getCustomerOrders_throwsPropagatesError() {
        Customer customer = CustomerFixtures.randomCustomer();

        Stubs.orderServiceStub()
                .stubGetCustomerOrders(500, customer.id().toString(), Optional.empty());

        Throwable result = catchThrowable(() -> underTest()
                .getCustomerOrders(customer.id(), UUID.randomUUID().toString())
                .collectList().block());

        assertThat(result).isInstanceOf(WebClientResponseException.class);
        assertThat(result.getMessage()).contains("500 Internal Server Error");
    }

    @Test
    void createOrder() {
        Order order = OrderFixtures.randomOrder();

        Stubs.orderServiceStub()
                .stubCreateOrder(204);

        underTest()
                .createOrder(Mono.just(order), UUID.randomUUID().toString())
                .block();

        RequestDefinition[] createOrderRequests = Stubs.orderServiceStub().retrieveCreateOrderRequests();
        assertThat(createOrderRequests).hasSize(1);
        Order receivedOrder = jsonUtil().fromJson(((HttpRequest) createOrderRequests[0]).getBodyAsJsonOrXmlString(), Order.class);
        assertThat(receivedOrder).isEqualTo(order);
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
        Map<UUID, Order> mappedReceivedOrders = Arrays.stream(receivedOrders).collect(Collectors.toMap(Order::id, it -> it));
        assertThat(mappedReceivedOrders.get(order1.id())).isEqualTo(order1);
        assertThat(mappedReceivedOrders.get(order2.id())).isEqualTo(order2);
    }

}
