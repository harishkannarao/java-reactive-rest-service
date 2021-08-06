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
        List<String> finalArgs = Stream.concat(inputArgs.stream(), additionalArgs.stream()).collect(Collectors.toList());
        SpringBootTestRunner.start(finalArgs);
    }
}
