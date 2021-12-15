package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.json.JsonUtil;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.MockServerTestRunner;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.PostgresTestRunner;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.SpringBootTestRunner;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.TestSupportExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.util.SocketUtils;

import java.util.Properties;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;

@ExtendWith({TestSupportExtension.class})
public abstract class AbstractBaseIntegrationTest {

    protected static final int RANDOM_SERVER_PORT = SocketUtils.findAvailableTcpPort();

    @BeforeEach
    void globalSetup() {
        if (!PostgresTestRunner.isRunning()) {
            PostgresTestRunner.start();
        }
        if (!MockServerTestRunner.isRunning()) {
            MockServerTestRunner.start();
        }
        if (!SpringBootTestRunner.isRunning()) {
            SpringBootTestRunner.start(getIntegrationTestProperties());
        } else if (!getIntegrationTestProperties().equals(SpringBootTestRunner.getProperties())) {
            SpringBootTestRunner.restart(getIntegrationTestProperties());
        }

        MockServerTestRunner.getClient().reset();
        customerApiClient().deleteAll().expectStatus().isNoContent();
    }

    protected Properties getIntegrationTestProperties() {
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

    protected <T> T getBean(Class<T> clazz) {
        return SpringBootTestRunner.getBean(clazz);
    }

    protected JsonUtil jsonUtil() {
        return getBean(JsonUtil.class);
    }
}
