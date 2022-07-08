package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.mockserver.client.MockServerClient;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.utility.DockerImageName;

public class MockServerTestRunner {

    private static final MockServerContainer CONTAINER = new MockServerContainer(DockerImageName.parse("mockserver/mockserver:mockserver-5.11.2"))
            .withEnv("MOCKSERVER_MAX_EXPECTATIONS", "300")
            .withEnv("MOCKSERVER_MAX_LOG_ENTRIES", "300")
            .withEnv("MOCKSERVER_MAX_WEB_SOCKET_EXPECTATIONS", "300")
            .withEnv("MOCKSERVER_LOG_LEVEL", "INFO")
            .withEnv("MOCKSERVER_DISABLE_SYSTEM_OUT", "false");

    private static MockServerClient MOCK_SERVER_CLIENT;

    public static void start() {
        CONTAINER.start();
        MOCK_SERVER_CLIENT = new MockServerClient(getHost(), getPort());
    }

    public static boolean isRunning() {
        return CONTAINER.isRunning();
    }

    public static void stop() {
        MOCK_SERVER_CLIENT.close();
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
        return MOCK_SERVER_CLIENT;
    }
}
