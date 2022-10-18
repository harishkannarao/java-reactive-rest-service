package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.json.JsonUtil;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.MockServerTestRunner;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.PostgresTestRunner;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.SpringBootTestRunner;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.util.SocketUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;

public abstract class AbstractBaseIntegrationTest {

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

    private Properties getIntegrationTestProperties() {
        Properties properties = SpringBootTestRunner.getIntegrationTestProperties();

        getAdditionalTestProperties().forEach(properties::setProperty);

        return properties;
    }

    protected Map<String, String> getAdditionalTestProperties() {
        return Collections.emptyMap();
    }

    protected <T> T getBean(Class<T> clazz) {
        return SpringBootTestRunner.getBean(clazz);
    }

    protected JsonUtil jsonUtil() {
        return getBean(JsonUtil.class);
    }
}
