package com.harishkannarao.java.spring.rest.javareactiverestservice.controller;

import com.harishkannarao.java.spring.rest.javareactiverestservice.client.OrderClient;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import com.harishkannarao.java.spring.rest.javareactiverestservice.repository.CustomerRepository;
import com.harishkannarao.java.spring.rest.javareactiverestservice.transformer.MonoTransformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public class CustomerOrderController {
    private final CustomerRepository customerRepository;
    private final OrderClient orderClient;

    @Autowired
    public CustomerOrderController(CustomerRepository customerRepository, OrderClient orderClient) {
        this.customerRepository = customerRepository;
        this.orderClient = orderClient;
    }

    @GetMapping(path = {"/customer/{customerId}/order"})
    public ResponseEntity<Flux<Order>> customerOrders(@PathVariable("customerId") UUID customerId) {
        Flux<Order> result = customerRepository.getCustomer(customerId)
                .transform(it -> MonoTransformers.errorOnEmpty(it, () -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(it -> orderClient.getCustomerOrders(it.getId())).flux()
                .flatMap(it -> it);
        return ResponseEntity.ok().body(result);
    }
}
