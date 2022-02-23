package com.harishkannarao.java.spring.rest.javareactiverestservice.config;

import com.harishkannarao.java.spring.rest.javareactiverestservice.exception.GlobalErrorWebExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.stream.Collectors;

@Configuration
public class GlobalExceptionHandlerConfiguration {

    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes,
                                                             WebProperties webProperties,
                                                             ObjectProvider<ViewResolver> viewResolvers,
                                                             ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext, ServerProperties serverProperties) {
        GlobalErrorWebExceptionHandler exceptionHandler = new GlobalErrorWebExceptionHandler(errorAttributes,
                webProperties,
                serverProperties.getError(), applicationContext);
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }
}
