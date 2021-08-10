package com.harishkannarao.java.spring.rest.javareactiverestservice.router;

import com.harishkannarao.java.spring.rest.javareactiverestservice.handler.CustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SuppressWarnings("NullableProblems")
@Configuration(proxyBeanMethods = false)
public class CustomerRouter {

    @Bean
    public RouterFunction<ServerResponse> getCustomerByIdRoute(CustomerHandler handler) {
        return route(GET("/customer/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::getCustomerById);
    }
}
