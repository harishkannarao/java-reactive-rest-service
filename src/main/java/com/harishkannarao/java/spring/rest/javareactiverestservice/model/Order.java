package com.harishkannarao.java.spring.rest.javareactiverestservice.model;

import java.util.UUID;

public record Order(
        UUID id,
        String description
) { }
