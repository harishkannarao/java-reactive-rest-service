package com.harishkannarao.java.spring.rest.javareactiverestservice.fixture;

import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;

import java.util.UUID;

public class OrderFixtures {

    public static Order randomOrder() {
        return new Order(UUID.randomUUID(), "description-" + UUID.randomUUID());
    }
}
