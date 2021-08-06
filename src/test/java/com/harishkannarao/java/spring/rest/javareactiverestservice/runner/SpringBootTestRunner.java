package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import com.harishkannarao.java.spring.rest.javareactiverestservice.JavaReactiveRestServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpringBootTestRunner {
    private static ConfigurableApplicationContext context;
    private static String[] properties;

    public static void stop() {
        if (isRunning()) {
            SpringApplication.exit(context);
        }
    }

    public static void start(List<String> args) {
        String[] propertiesArray = args.toArray(String[]::new);
        context = SpringApplication.run(JavaReactiveRestServiceApplication.class, propertiesArray);
        properties = propertiesArray;
    }

    public static void restart(List<String> properties) {
        stop();
        start(properties);
    }

    public static boolean isRunning() {
        return Optional.ofNullable(context)
                .map(Lifecycle::isRunning)
                .orElse(false);
    }

    public static List<String> getProperties() {
        if (properties != null) {
            return Stream.of(properties).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static String getPort() {
        return getBean(Environment.class).getProperty("local.server.port");
    }

    public static String getApplicationUrl() {
        return String.format("http://localhost:%s", getPort());
    }
}
