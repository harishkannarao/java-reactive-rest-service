package com.harishkannarao.java.spring.rest.javareactiverestservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FileSystemStaticConfiguration {

    private final Environment env;

    @Autowired
    public FileSystemStaticConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    @ConditionalOnProperty(prefix = "file-system-static-content", name = "enabled", havingValue = "true")
    public RouterFunction<ServerResponse> fileSystemRouter() {
        return RouterFunctions
                .resources("/**", new FileSystemResource(env.getRequiredProperty("file-system-static-content.path")));
    }
}
