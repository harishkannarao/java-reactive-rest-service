package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalRunnerWithFixtures {

    public static void main(String[] args) {
        PostgresTestRunner.start();
        List<String> inputArgs = Arrays.asList(args);
        List<String> finalArgs = new ArrayList<>();
        finalArgs.addAll(inputArgs);
        finalArgs.addAll(getPostgresTestProperties());
        finalArgs.addAll(getSpringTestProperties());
        SpringBootTestRunner.start(finalArgs);
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
