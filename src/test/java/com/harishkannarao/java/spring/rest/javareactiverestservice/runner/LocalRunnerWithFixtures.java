package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.OrderFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.json.JsonUtil;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import com.harishkannarao.java.spring.rest.javareactiverestservice.stub.Stubs;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;

public class LocalRunnerWithFixtures {

    public static void main(String[] args) {
        Supplier<Boolean> postgresStarter = () -> {
            PostgresTestRunner.start();
            return true;
        };
        Supplier<Boolean> mockServerStarter = () -> {
            MockServerTestRunner.start();
            return true;
        };
        Stream.of(postgresStarter, mockServerStarter)
                .parallel()
                .map(Supplier::get)
                .forEach(aBoolean -> {});

        var props = new Properties();
        props.setProperty("postgresql-datasource.timeout", "10");
        props.setProperty("postgresql-datasource.host", PostgresTestRunner.getHost());
        props.setProperty("postgresql-datasource.port", String.valueOf(PostgresTestRunner.getPort()));
        props.setProperty("postgresql-datasource.database", PostgresTestRunner.getUsername());
        props.setProperty("postgresql-datasource.username", PostgresTestRunner.getUsername());
        props.setProperty("postgresql-datasource.password", PostgresTestRunner.getPassword());
        props.setProperty("spring.flyway.url", PostgresTestRunner.getJdbcUrl());
        props.setProperty("spring.flyway.user", PostgresTestRunner.getUsername());
        props.setProperty("spring.flyway.password", PostgresTestRunner.getPassword());
        props.setProperty("order-service.base-url", MockServerTestRunner.getUrl());
        props.setProperty("webclient.timeout-seconds", "5");
        props.setProperty("server.port", "8080");
        props.setProperty("spring.profiles.active", "int-test");

        SpringBootTestRunner.start(props);

        createFixtures();
    }

    private static void createFixtures() {
        JsonUtil jsonUtil = SpringBootTestRunner.getBean(JsonUtil.class);
        Customer customer1 = CustomerFixtures.randomCustomer();
        Customer customer2 = CustomerFixtures.randomCustomer();
        List<Customer> customers = List.of(customer1, customer2);
        customerApiClient().createMultiple(customers)
                        .expectStatus().isOk();

        Order order1 = OrderFixtures.randomOrder();
        Order order2 = OrderFixtures.randomOrder();
        List<Order> orders = List.of(order1, order2);

        customers.forEach(it -> Stubs.orderServiceStub()
                .stubGetCustomerOrders(200,
                        it.id().toString(),
                        Optional.of(jsonUtil.toJson(orders))));

        Stubs.orderServiceStub()
                .stubGetOrders(200, jsonUtil.toJson(orders));

        Stubs.orderServiceStub()
                .stubCreateOrder(204);

        Stubs.orderServiceStub()
                .stubCreateOrder(204);

        Stubs.orderServiceStub()
                .stubDeleteOrders(204);
    }
}
