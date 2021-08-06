package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresTestRunner {

    private static final int PORT = 5432;
    private static final String USERNAME = "test-user";
    private static final String PASSWORD = "test-password";
    private static final GenericContainer CONTAINER = new GenericContainer(DockerImageName.parse("postgres:11-alpine"))
            .withExposedPorts(PORT)
            .withEnv("POSTGRES_USER", USERNAME)
            .withEnv("POSTGRES_PASSWORD", PASSWORD);

    public static void start() {
        CONTAINER.start();
    }

    public static boolean isRunning() {
        return CONTAINER.isRunning();
    }

    public static void stop() {
        CONTAINER.stop();
    }

    public static String getHost() {
        return CONTAINER.getHost();
    }

    public static Integer getPort() {
        return CONTAINER.getMappedPort(PORT);
    }

    public static String getUsername() {
        return USERNAME;
    }

    public static String getPassword() {
        return PASSWORD;
    }
}
