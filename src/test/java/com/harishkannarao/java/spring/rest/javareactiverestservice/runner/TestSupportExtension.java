package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.junit.jupiter.api.extension.*;
import org.springframework.util.SocketUtils;

public class TestSupportExtension implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, AfterEachCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
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
    }

    @Override
    public void beforeEach(ExtensionContext context) {

    }

    @Override
    public void afterAll(ExtensionContext context) {
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

    @Override
    public void afterEach(ExtensionContext context) {

    }
}
