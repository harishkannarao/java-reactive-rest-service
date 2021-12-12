package com.harishkannarao.java.spring.rest.javareactiverestservice.runner;

import com.harishkannarao.java.spring.rest.javareactiverestservice.JavaReactiveRestServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.core.env.Environment;

import java.util.Optional;
import java.util.Properties;

public class SpringBootTestRunner {
    private static ConfigurableApplicationContext context;
    private static Properties properties;

    public static void stop() {
        if (isRunning()) {
            SpringApplication.exit(context);
        }
    }

    public static void start(Properties props) {
        var application = new SpringApplication(JavaReactiveRestServiceApplication.class);
        application.setDefaultProperties(props);
        context = application.run();
        properties = props;
    }

    public static void restart(Properties props) {
        stop();
        start(props);
    }

    public static boolean isRunning() {
        return Optional.ofNullable(context)
                .map(Lifecycle::isRunning)
                .orElse(false);
    }

    public static Properties getProperties() {
        return Optional.ofNullable(properties).orElseGet(Properties::new);
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static String getPort() {
        return getBean(Environment.class).getProperty("server.port");
    }

    public static String getApplicationUrl() {
        return String.format("http://localhost:%s", getPort());
    }
}
