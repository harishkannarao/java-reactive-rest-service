package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class TestSupportExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static final AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if (!started.get()) {
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
