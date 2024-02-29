package com.harishkannarao.java.spring.rest.javareactiverestservice.config;

import com.harishkannarao.java.spring.rest.javareactiverestservice.filter.WebClientLoggingFilter;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient createWebClient(
            WebClient.Builder webClientBuilder,
            WebClientLoggingFilter webClientLoggingFilter,
            @Value("${webclient.timeout-seconds}") Integer timeoutSeconds
    ) {
        return webClientBuilder
                .filter(webClientLoggingFilter)
                .clientConnector(
                        new ReactorClientHttpConnector(
                                HttpClient.create()
                                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutSeconds * 1000)
                                        .doOnConnected(c ->
                                                c.addHandlerLast(new ReadTimeoutHandler(timeoutSeconds))
                                                        .addHandlerLast(new WriteTimeoutHandler(timeoutSeconds))
                                        )
                        )
                )
                .build();
    }
}
