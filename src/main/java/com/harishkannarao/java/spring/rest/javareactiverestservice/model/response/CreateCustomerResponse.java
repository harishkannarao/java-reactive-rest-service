package com.harishkannarao.java.spring.rest.javareactiverestservice.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CreateCustomerResponse {

    private final UUID id;

    @JsonCreator
    public CreateCustomerResponse(
            @JsonProperty("id") UUID id) {
        this.id = id;
    }

    @JsonProperty("id")
    public UUID getId() {
        return id;
    }
}
