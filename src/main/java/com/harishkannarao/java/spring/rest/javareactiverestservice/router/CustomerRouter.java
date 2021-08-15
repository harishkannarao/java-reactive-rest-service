package com.harishkannarao.java.spring.rest.javareactiverestservice.router;

import com.harishkannarao.java.spring.rest.javareactiverestservice.handler.CustomerHandler;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SuppressWarnings("NullableProblems")
@Configuration(proxyBeanMethods = false)
public class CustomerRouter {

    @Bean
    @RouterOperation(
            operation = @Operation(
                    operationId = "getCustomerById", summary = "Find customer by ID", tags = { "customer" },
                    parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Customer Id")},
                    responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Customer.class))),
                            @ApiResponse(responseCode = "400", description = "Invalid Customer ID supplied"),
                            @ApiResponse(responseCode = "404", description = "Customer not found")
                    }
            )
    )
    public RouterFunction<ServerResponse> getCustomerByIdRoute(CustomerHandler handler) {
        return route(GET("/customer/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::getCustomerById);
    }

    @Bean
    public RouterFunction<ServerResponse> createCustomerRoute(CustomerHandler handler) {
        return route(POST("/customer").and(accept(MediaType.APPLICATION_JSON)), handler::createCustomer);
    }

    @Bean
    public RouterFunction<ServerResponse> createMultipleCustomersRoute(CustomerHandler handler) {
        return route(POST("/customer/create-multiple").and(accept(MediaType.APPLICATION_JSON)), handler::createMultipleCustomers);
    }

    @Bean
    public RouterFunction<ServerResponse> getAllCustomersRoute(CustomerHandler handler) {
        return route(GET("/customer").and(accept(MediaType.APPLICATION_JSON)), handler::getAllCustomers);
    }

    @Bean
    public RouterFunction<ServerResponse> deleteAllCustomersRoute(CustomerHandler handler) {
        return route(DELETE("/customer").and(accept(MediaType.APPLICATION_JSON)), handler::deleteAllCustomers);
    }
}
