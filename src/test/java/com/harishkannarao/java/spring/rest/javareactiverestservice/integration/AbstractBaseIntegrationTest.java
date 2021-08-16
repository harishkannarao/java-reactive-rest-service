package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.PostgresTestRunner;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.ShutdownExtension;
import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.SpringBootTestRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.client.Clients.customerApiClient;

@ExtendWith({ShutdownExtension.class})
public abstract class AbstractBaseIntegrationTest {

    @BeforeEach
    void globalSetup() {
        customerApiClient().deleteAll().expectStatus().isNoContent();
    }

    private List<String> getPostgresTestProperties() {
        return List.of(
                "--postgresql-datasource.host=" + PostgresTestRunner.getHost(),
                "--postgresql-datasource.port=" + PostgresTestRunner.getPort(),
                "--postgresql-datasource.database=" + PostgresTestRunner.getUsername(),
                "--postgresql-datasource.username=" + PostgresTestRunner.getUsername(),
                "--postgresql-datasource.password=" + PostgresTestRunner.getPassword(),
                "--spring.flyway.url=" + PostgresTestRunner.getJdbcUrl(),
                "--spring.flyway.user=" + PostgresTestRunner.getUsername(),
                "--spring.flyway.password=" + PostgresTestRunner.getPassword()
        );
    }

    private List<String> getSpringTestProperties() {
        return List.of(
                "--spring.profiles.active=int-test",
                "--server.port=0"
        );
    }

    private List<String> getIntegrationTestProperties() {
        return Stream.concat(
                        getPostgresTestProperties().stream(),
                        getSpringTestProperties().stream()
                )
                .collect(Collectors.toList());
    }

    @BeforeEach
    public void resetApplication() {
        if (!PostgresTestRunner.isRunning()) {
            PostgresTestRunner.start();
        }
        if (!SpringBootTestRunner.isRunning()) {
            SpringBootTestRunner.start(getIntegrationTestProperties());
        }
    }

    protected <T> T getBean(Class<T> clazz) {
        return SpringBootTestRunner.getBean(clazz);
    }
}
