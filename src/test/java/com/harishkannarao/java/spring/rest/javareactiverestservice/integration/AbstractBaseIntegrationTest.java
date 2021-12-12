package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.json.JsonUtil;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.MockServerTestRunner;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.SpringBootTestRunner;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.TestSupportExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Properties;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;

@ExtendWith({TestSupportExtension.class})
public abstract class AbstractBaseIntegrationTest {

    @BeforeEach
    void globalSetup() {
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
        properties.setProperty("spring.profiles.active", "int-test");
        return properties;
    }

    protected <T> T getBean(Class<T> clazz) {
        return SpringBootTestRunner.getBean(clazz);
    }

    protected JsonUtil jsonUtil() {
        return getBean(JsonUtil.class);
    }
}
