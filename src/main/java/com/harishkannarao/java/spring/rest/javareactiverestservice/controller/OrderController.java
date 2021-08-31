package com.harishkannarao.java.spring.rest.javareactiverestservice.controller;

import com.harishkannarao.java.spring.rest.javareactiverestservice.client.OrderClient;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
    public ResponseEntity<Flux<Order>> orders(@RequestParam(name = "limit") Optional<Integer> limit) {
        return ResponseEntity.ok().body(orderClient.getOrders(Optional.empty(), limit));
    }
}
