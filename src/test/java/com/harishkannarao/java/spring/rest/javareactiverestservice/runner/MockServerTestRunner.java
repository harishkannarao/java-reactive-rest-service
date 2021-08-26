package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.mockserver.client.MockServerClient;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.utility.DockerImageName;

public class MockServerTestRunner {

    private static final MockServerContainer CONTAINER = new MockServerContainer(DockerImageName.parse("mockserver/mockserver:mockserver-5.11.2"));

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
        return CONTAINER.getServerPort();
    }

    public static String getUrl() {
        return String.format("http://%s:%s", getHost(), getPort());
    }

    public static MockServerClient getClient() {
        return new MockServerClient(getHost(), getPort());
    }
}
