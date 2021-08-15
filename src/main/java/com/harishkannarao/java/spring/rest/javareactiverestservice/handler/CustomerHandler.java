package com.harishkannarao.java.spring.rest.javareactiverestservice.handler;

import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.response.CreateCustomerResponse;
import com.harishkannarao.java.spring.rest.javareactiverestservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
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
        UUID customerId;
        try {
            customerId = UUID.fromString(request.pathVariable("id"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        Mono<Customer> customer = customerRepository.getCustomer(customerId)
                .transform(it -> it.hasElement().flatMap(result -> {
                    if (result) {
                        return it;
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
                    }
                }));
        return ServerResponse.ok().body(customer, Customer.class);
    }

    public Mono<ServerResponse> createCustomer(ServerRequest request) {
        Mono<Customer> customerMono = request.bodyToMono(Customer.class);
        Mono<UUID> uuidMono = customerMono.flatMap(customerRepository::createCustomer);
        Mono<CreateCustomerResponse> createCustomerResponseMono = uuidMono.map(CreateCustomerResponse::new);
        return ServerResponse.ok().body(createCustomerResponseMono, CreateCustomerResponse.class);
    }

    public Mono<ServerResponse> createMultipleCustomers(ServerRequest request) {
        Flux<Customer> customerFlux = request.bodyToFlux(Customer.class);
        Flux<UUID> uuidFlux = customerFlux.flatMap(customerRepository::createCustomer);
        Flux<CreateCustomerResponse> result = uuidFlux.map(CreateCustomerResponse::new);
        return ServerResponse.ok().body(result, CreateCustomerResponse.class);
    }

    public Mono<ServerResponse> getAllCustomers(ServerRequest request) {
        Flux<Customer> allCustomers = customerRepository.listCustomers();
        return ServerResponse.ok().body(allCustomers, Customer.class);
    }

    public Mono<ServerResponse> deleteAllCustomers(ServerRequest request) {
        Mono<Void> response = customerRepository.deleteAllCustomers().then();
        return ServerResponse.ok().body(response, Void.class);
    }
}
