package com.harishkannarao.java.spring.rest.javareactiverestservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Order {

    private final UUID id;
    private final String description;

    @JsonCreator
    public Order(
            @JsonProperty("id") UUID id,
            @JsonProperty("description") String description
    ) {
        this.id = id;
        this.description = description;
    }

    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }
}
