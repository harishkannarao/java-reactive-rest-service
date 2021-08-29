package com.harishkannarao.java.spring.rest.javareactiverestservice.controller;

import com.harishkannarao.java.spring.rest.javareactiverestservice.client.OrderClient;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

@RestController
public class CustomerOrderController {
    private final OrderClient orderClient;

    @Autowired
    public CustomerOrderController(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @GetMapping(path = {"/customer/{customerId}/order"})
    public ResponseEntity<Flux<Order>> customerOrders(@PathVariable("customerId") UUID customerId) {
        return ResponseEntity.ok().body(orderClient.getCustomerOrders(customerId));
    }
}
