package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.ShutdownExtension;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.SpringBootTestRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@ExtendWith({ShutdownExtension.class})
public abstract class AbstractBaseIntegrationTest {

    private List<String> getIntegrationTestProperties() {
        return List.of(
                "--spring.profiles.active=int-test",
                "--server.port=8081"
        );
    }

    @BeforeEach
    public void resetApplication() {
        if (!SpringBootTestRunner.isRunning()) {
            SpringBootTestRunner.start(getIntegrationTestProperties());
        }
    }

    protected WebTestClient webTestClient() {
        return WebTestClient.bindToServer().baseUrl(SpringBootTestRunner.getApplicationUrl()).build();
    }
}
