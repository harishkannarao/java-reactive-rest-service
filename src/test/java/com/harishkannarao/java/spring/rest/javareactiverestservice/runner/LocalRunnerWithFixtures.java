package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;

public class LocalRunnerWithFixtures {

    public static void main(String[] args) {
        PostgresTestRunner.start();
        MockServerTestRunner.start();

        List<String> finalArgs = Stream.of(
                        getPostgresTestProperties(),
                        getSpringTestProperties(),
                        Arrays.asList(args)
                ).flatMap(Collection::stream)
                .collect(Collectors.toList());
        SpringBootTestRunner.start(finalArgs);

        createCustomerFixtures();
    }

    private static void createCustomerFixtures() {
        Customer customer1 = CustomerFixtures.randomCustomer();
        Customer customer2 = CustomerFixtures.randomCustomer();
        customerApiClient().createMultiple(List.of(customer1, customer2))
                        .expectStatus().isOk();
    }

    private static List<String> getPostgresTestProperties() {
        return List.of(
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


    private static List<String> getSpringTestProperties() {
        return List.of(
                "--spring.profiles.active=int-test",
                "--server.port=8080"
        );
    }
}
