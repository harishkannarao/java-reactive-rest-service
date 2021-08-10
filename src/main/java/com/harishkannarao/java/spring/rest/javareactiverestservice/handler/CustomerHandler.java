package com.harishkannarao.java.spring.rest.javareactiverestservice.handler;

import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CustomerHandler {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerHandler(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<ServerResponse> getCustomerById(ServerRequest request) {
        UUID customerId = UUID.fromString(request.pathVariable("id"));
        Mono<Customer> customer = customerRepository.getCustomer(customerId);
        return ServerResponse.ok().body(customer, Customer.class);
    }
}
