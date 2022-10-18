package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import com.harishkannarao.java.spring.rest.javareactiverestservice.JavaReactiveRestServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.core.env.Environment;
import org.springframework.util.SocketUtils;

import java.util.Optional;
import java.util.Properties;

public class SpringBootTestRunner {
    protected static final int RANDOM_SERVER_PORT = SocketUtils.findAvailableTcpPort();
    private static ConfigurableApplicationContext context;
    private static Properties properties;

    public static void stop() {
        if (isRunning()) {
            SpringApplication.exit(context);
        }
    }

    public static void start(Properties props) {
        String[] args = props.entrySet().stream()
                .map(entry -> String.format("--%s=%s", entry.getKey(), entry.getValue()))
                .toArray(String[]::new);
        context = SpringApplication.run(JavaReactiveRestServiceApplication.class, args);
        properties = props;
    }

    public static void restart(Properties props) {
        stop();
        start(props);
    }

    public static boolean isRunning() {
        return Optional.ofNullable(context)
                .map(Lifecycle::isRunning)
                .orElse(false);
    }

    public static Properties getProperties() {
        return Optional.ofNullable(properties).orElseGet(Properties::new);
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static String getPort() {
        return getBean(Environment.class).getProperty("server.port");
    }

    public static String getApplicationUrl() {
        return String.format("http://localhost:%s", getPort());
    }

    public static Properties getIntegrationTestProperties() {
        Properties properties = new Properties();
        properties.setProperty("server.port", String.valueOf(RANDOM_SERVER_PORT));
        properties.setProperty("spring.profiles.active", "int-test");
        properties.setProperty("postgresql-datasource.timeout", "10");
        properties.setProperty("postgresql-datasource.host", PostgresTestRunner.getHost());
        properties.setProperty("postgresql-datasource.port", String.valueOf(PostgresTestRunner.getPort()));
        properties.setProperty("postgresql-datasource.database", PostgresTestRunner.getUsername());
        properties.setProperty("postgresql-datasource.username", PostgresTestRunner.getUsername());
        properties.setProperty("postgresql-datasource.password", PostgresTestRunner.getPassword());
        properties.setProperty("spring.flyway.url", PostgresTestRunner.getJdbcUrl());
        properties.setProperty("spring.flyway.user", PostgresTestRunner.getUsername());
        properties.setProperty("spring.flyway.password", PostgresTestRunner.getPassword());
        properties.setProperty("order-service.base-url", MockServerTestRunner.getUrl());
        properties.setProperty("order-service.timeout-seconds", "1");

        return properties;
    }
}
