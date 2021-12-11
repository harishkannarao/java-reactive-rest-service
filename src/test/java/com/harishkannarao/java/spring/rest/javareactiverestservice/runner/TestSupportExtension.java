package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.junit.jupiter.api.extension.*;
import org.springframework.util.SocketUtils;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class TestSupportExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if (!started.get()) {
            PostgresTestRunner.start();
            System.setProperty("postgresql-datasource.timeout", "10");
            System.setProperty("postgresql-datasource.host", PostgresTestRunner.getHost());
            System.setProperty("postgresql-datasource.port", String.valueOf(PostgresTestRunner.getPort()));
            System.setProperty("postgresql-datasource.database", PostgresTestRunner.getUsername());
            System.setProperty("postgresql-datasource.username", PostgresTestRunner.getUsername());
            System.setProperty("postgresql-datasource.password", PostgresTestRunner.getPassword());
            System.setProperty("spring.flyway.url", PostgresTestRunner.getJdbcUrl());
            System.setProperty("spring.flyway.user", PostgresTestRunner.getUsername());
            System.setProperty("spring.flyway.password", PostgresTestRunner.getPassword());

            MockServerTestRunner.start();
            System.setProperty("order-service.base-url", MockServerTestRunner.getUrl());
            System.setProperty("order-service.timeout-seconds", "5");

            System.setProperty("server.port", String.valueOf(SocketUtils.findAvailableTcpPort()));

            started.set(true);
            // Your "before all tests" startup logic goes here
            // The following line registers a callback hook when the root test context is shut down
            extensionContext.getRoot().getStore(GLOBAL).put("SHUTDOWN_EXTENSION", this);
        }
    }

    @Override
    public void close() {
        if (SpringBootTestRunner.isRunning()) {
            try {
                SpringBootTestRunner.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (MockServerTestRunner.isRunning()) {
            try {
                MockServerTestRunner.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (PostgresTestRunner.isRunning()) {
            try {
                PostgresTestRunner.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
