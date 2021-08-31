package com.harishkannarao.java.spring.rest.javareactiverestservice.controller;

import com.harishkannarao.java.spring.rest.javareactiverestservice.client.OrderClient;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
public class OrderController {
    private final OrderClient orderClient;

    @Autowired
    public OrderController(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @GetMapping(path = {"/order"})
    public ResponseEntity<Flux<Order>> getOrders(@RequestParam(name = "limit") Optional<Integer> limit) {
        return ResponseEntity.ok().body(orderClient.getOrders(Optional.empty(), limit));
    }

    @PostMapping(path = {"/order"})
    public ResponseEntity<Mono<Void>> createOrder(@RequestBody Order order) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(orderClient.createOrder(order));
    }
}
