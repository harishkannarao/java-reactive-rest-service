package com.harishkannarao.java.spring.rest.javareactiverestservice.router;

import com.harishkannarao.java.spring.rest.javareactiverestservice.handler.CustomerHandler;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.response.CreateCustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    @Bean
    public RouterFunction<ServerResponse> getCustomerByIdRoute(CustomerHandler handler) {
        return route(GET("/customer/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::getCustomerById);
    }

    @RouterOperation(
            operation = @Operation(
                    operationId = "createCustomer", summary = "create a customer", tags = { "customer" },
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Customer.class))),
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "successful operation",
                                    content = @Content(schema = @Schema(implementation = CreateCustomerResponse.class)))
                    }
            )
    )
    @Bean
    public RouterFunction<ServerResponse> createCustomerRoute(CustomerHandler handler) {
        return route(POST("/customer").and(accept(MediaType.APPLICATION_JSON)), handler::createCustomer);
    }

    @RouterOperation(
            operation = @Operation(
                    operationId = "createMultipleCustomers", summary = "create multiple customers", tags = { "customer" },
                    requestBody = @RequestBody(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Customer.class)))),
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "successful operation",
                                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CreateCustomerResponse.class))))
                    }
            )
    )
    @Bean
    public RouterFunction<ServerResponse> createMultipleCustomersRoute(CustomerHandler handler) {
        return route(POST("/customer/create-multiple").and(accept(MediaType.APPLICATION_JSON)), handler::createMultipleCustomers);
    }

    @RouterOperation(
            operation = @Operation(
                    operationId = "getAllCustomers", summary = "List all customers", tags = { "customer" },
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "successful operation",
                                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Customer.class))))
                    }
            )
    )
    @Bean
    public RouterFunction<ServerResponse> getAllCustomersRoute(CustomerHandler handler) {
        return route(GET("/customer").and(accept(MediaType.APPLICATION_JSON)), handler::getAllCustomers);
    }

    @RouterOperation(
            operation = @Operation(
                    operationId = "deleteAllCustomers", summary = "Delete All Customers", tags = { "customer" },
                    responses = {
                            @ApiResponse(responseCode = "204", description = "successful operation")
                    }
            )
    )
    @Bean
    public RouterFunction<ServerResponse> deleteAllCustomersRoute(CustomerHandler handler) {
        return route(DELETE("/customer").and(accept(MediaType.APPLICATION_JSON)), handler::deleteAllCustomers);
    }
}
