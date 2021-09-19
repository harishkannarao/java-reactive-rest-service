package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.OrderFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.json.JsonUtil;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import com.harishkannarao.java.spring.rest.javareactiverestservice.stub.Stubs;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;

public class LocalRunnerWithFixtures {

    public static void main(String[] args) {
        PostgresTestRunner.start();
        MockServerTestRunner.start();

        List<String> finalArgs = Stream.of(
                        getPostgresTestProperties(),
                        getMockServerTestProperties(),
                        getSpringTestProperties(),
                        Arrays.asList(args)
                ).flatMap(Collection::stream)
                .collect(Collectors.toList());
        SpringBootTestRunner.start(finalArgs);

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
        Integer limit = 10;

        customers.forEach(it -> Stubs.orderServiceStub()
                .stubGetCustomerOrders(200,
                        it.getId().toString(),
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

    private static List<String> getPostgresTestProperties() {
        return List.of(
                "--postgresql-datasource.timeout=10",
                "--postgresql-datasource.host=" + PostgresTestRunner.getHost(),
                "--postgresql-datasource.port=" + PostgresTestRunner.getPort(),
                "--postgresql-datasource.database=" + PostgresTestRunner.getUsername(),
                "--postgresql-datasource.username=" + PostgresTestRunner.getUsername(),
                "--postgresql-datasource.password=" + PostgresTestRunner.getPassword(),
                "--spring.flyway.url=" + PostgresTestRunner.getJdbcUrl(),
                "--spring.flyway.user=" + PostgresTestRunner.getUsername(),
                "--spring.flyway.password=" + PostgresTestRunner.getPassword()
        );
    }

    private static List<String> getMockServerTestProperties() {
        return List.of(
                "--order-service.base-url=" + MockServerTestRunner.getUrl()
        );
    }

    private static List<String> getSpringTestProperties() {
        return List.of(
                "--spring.profiles.active=int-test",
                "--server.port=8080"
        );
    }
}
