package com.harishkannarao.java.spring.rest.javareactiverestservice.fixture;

import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;

import java.util.UUID;

public class CustomerFixtures {

    public static Customer randomCustomer() {
        return new Customer(
                UUID.randomUUID(),
                "firstName-" + UUID.randomUUID(),
                "lastName-" + UUID.randomUUID()
        );
    }
}
