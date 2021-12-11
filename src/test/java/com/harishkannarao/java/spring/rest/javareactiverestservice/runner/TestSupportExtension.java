package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.junit.jupiter.api.extension.*;

public class TestSupportExtension implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, AfterEachCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        PostgresTestRunner.start();
        MockServerTestRunner.start();
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

    @Override
    public void beforeEach(ExtensionContext context) {

    }
}
