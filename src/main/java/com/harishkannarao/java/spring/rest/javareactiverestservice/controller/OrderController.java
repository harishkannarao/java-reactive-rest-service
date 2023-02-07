package com.harishkannarao.java.spring.rest.javareactiverestservice.controller;

import com.harishkannarao.java.spring.rest.javareactiverestservice.client.OrderClient;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.server.ServerWebExchange.LOG_ID_ATTRIBUTE;

@SuppressWarnings({"unused"})
@RestController
public class OrderController {
    private final OrderClient orderClient;

    @Autowired
    public OrderController(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @GetMapping(path = {"/order"})
    public ResponseEntity<Flux<Order>> getOrders(
            @RequestParam(name = "limit") Optional<Integer> limit,
            ServerWebExchange serverWebExchange) {
        return ResponseEntity.ok()
                .body(
                        orderClient.getOrders(
                                limit,
                                serverWebExchange.getRequiredAttribute(LOG_ID_ATTRIBUTE)));
    }

    @GetMapping(path = {"/order/{id}"})
    public Mono<Order> getOrders(
            @PathVariable(name = "id") UUID id,
            ServerWebExchange serverWebExchange) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
        Mono<Order> order = orderClient.getOrder(id, serverWebExchange.getRequiredAttribute(LOG_ID_ATTRIBUTE));
        return order.map(value -> {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
            return value;
        });
    }

    @PostMapping(path = {"/order"})
    public ResponseEntity<Mono<Void>> createOrder(@RequestBody(required = false) Mono<Order> order, ServerWebExchange serverWebExchange) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(
                        orderClient.createOrder(order, serverWebExchange.getRequiredAttribute(LOG_ID_ATTRIBUTE)));
    }

    @PostMapping(path = {"/order/delete"})
    public ResponseEntity<Mono<Void>> deleteOrders(
            @RequestHeader(name = "maxOrdersInPayload") Integer maxOrdersInPayload,
            @RequestBody(required = false) Flux<Order> orders,
            ServerWebExchange serverWebExchange) {
        if (maxOrdersInPayload > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(
                        orderClient.deleteOrders(
                                orders.take(maxOrdersInPayload, true),
                                serverWebExchange.getRequiredAttribute(LOG_ID_ATTRIBUTE))
                );
    }
}
