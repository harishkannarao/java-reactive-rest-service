package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalRunnerWithFixtures {

    public static void main(String[] args) {
        PostgresTestRunner.start();
        List<String> inputArgs = Arrays.asList(args);
        List<String> additionalArgs = List.of(
                "--spring.profiles.active=int-test",
                "--server.port=8080"
        );
        List<String> finalArgs = Stream.concat(
                inputArgs.stream(),
                Stream.concat(getPostgresTestProperties().stream(), getSpringTestProperties().stream())
        ).collect(Collectors.toList());
        SpringBootTestRunner.start(finalArgs);
    }

    private static List<String> getPostgresTestProperties() {
        return List.of(
                "--postgresql-datasource.host=" + PostgresTestRunner.getHost(),
                "--postgresql-datasource.port=" + PostgresTestRunner.getPort(),
                "--postgresql-datasource.database=" + PostgresTestRunner.getUsername(),
                "--postgresql-datasource.username=" + PostgresTestRunner.getUsername(),
                "--postgresql-datasource.password=" + PostgresTestRunner.getPassword()
        );
    }


    private static List<String> getSpringTestProperties() {
        return List.of(
                "--spring.profiles.active=int-test",
                "--server.port=8080"
        );
    }
}
