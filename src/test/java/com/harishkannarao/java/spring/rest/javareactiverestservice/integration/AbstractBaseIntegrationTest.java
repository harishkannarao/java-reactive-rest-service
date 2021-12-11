package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.json.JsonUtil;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.MockServerTestRunner;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.SpringBootTestRunner;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.TestSupportExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;

@ExtendWith({TestSupportExtension.class})
public abstract class AbstractBaseIntegrationTest {

    @BeforeEach
    void globalSetup() {
        if (!SpringBootTestRunner.isRunning()) {
            SpringBootTestRunner.start(getIntegrationTestProperties());
        }

        MockServerTestRunner.getClient().reset();
        customerApiClient().deleteAll().expectStatus().isNoContent();
    }

    protected List<String> getIntegrationTestProperties() {
        return List.of(
                "--spring.profiles.active=int-test"
        );
    }

    protected <T> T getBean(Class<T> clazz) {
        return SpringBootTestRunner.getBean(clazz);
    }

    protected JsonUtil jsonUtil() {
        return getBean(JsonUtil.class);
    }
}
