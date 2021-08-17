package com.harishkannarao.java.spring.rest.javareactiverestservice.transformer;

import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public class MonoTransformers {

    public static <T> Mono<T> errorOnEmpty(Mono<T> input, Supplier<RuntimeException> error) {
        return input.hasElement().flatMap(result -> {
            if (result) {
                return input;
            } else {
                throw error.get();
            }
        });
    }
}
