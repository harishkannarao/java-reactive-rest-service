package com.harishkannarao.java.spring.rest.javareactiverestservice.client;

import com.harishkannarao.java.spring.rest.javareactiverestservice.filter.WebClientLoggingFilter;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.filter.WebClientLoggingFilter.REQUEST_ID;

@Component
public class OrderClient {

    private final WebClient webClient;

    @Autowired
    public OrderClient(
            WebClient.Builder webClientBuilder,
            WebClientLoggingFilter webClientLoggingFilter,
            @Value("${order-service.base-url}") String orderServiceBaseUrl,
            @Value("${order-service.timeout-seconds}") Integer orderServiceTimeoutSeconds) {
        this.webClient = webClientBuilder
                .baseUrl(orderServiceBaseUrl)
                .filter(webClientLoggingFilter)
                .clientConnector(
                        new ReactorClientHttpConnector(
                                HttpClient.create()
                                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, orderServiceTimeoutSeconds * 1000)
                                        .doOnConnected(c ->
                                                c.addHandlerLast(new ReadTimeoutHandler(orderServiceTimeoutSeconds))
                                                .addHandlerLast(new WriteTimeoutHandler(orderServiceTimeoutSeconds))
                                        )
                        )
                )
                .build();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public Flux<Order> getOrders(Optional<Integer> limit, String requestId) {
        return webClient.get()
                .uri(uriBuilder -> {
                            UriBuilder builder = uriBuilder
                                    .path("/order");
                            Map<String, String> variables = new HashMap<>();
                            limit.ifPresent(it -> {
                                builder.queryParam("limit", "{limit}");
                                variables.put("limit", it.toString());
                            });
                            return builder.build(variables);
                        }
                )
                .attribute(REQUEST_ID, requestId)
                .retrieve()
                .bodyToFlux(Order.class);
    }

    public Mono<Order> getOrder(UUID id, String requestId) {
        return webClient.get()
                .uri(uriBuilder -> {
                            UriBuilder builder = uriBuilder
                                    .path("/order/{id}");
                            Map<String, String> variables = new HashMap<>();
                            variables.put("id", id.toString());
                            return builder.build(variables);
                        }
                )
                .attribute(REQUEST_ID, requestId)
                .retrieve()
                .bodyToMono(Order.class)
                .onErrorResume(WebClientResponseException.NotFound.class, notFound -> Mono.empty());
    }

    public Mono<Void> createOrder(Mono<Order> order, String requestId) {
        return order.flatMap(it -> webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/order").build())
                .attribute(REQUEST_ID, requestId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(it))
                .retrieve()
                .bodyToMono(Void.class));
    }

    public Mono<Void> deleteOrders(Flux<Order> orders, String requestId) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/order/delete").build())
                .attribute(REQUEST_ID, requestId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromProducer(orders, Order.class))
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Flux<Order> getCustomerOrders(UUID customerId, String requestId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/order/customer/{customerId}")
                        .build(Map.ofEntries(Map.entry("customerId", customerId)))
                )
                .attribute(REQUEST_ID, requestId)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.bodyToFlux(Order.class);
                    } else if (HttpStatus.NOT_FOUND.equals(clientResponse.statusCode())) {
                        return Flux.empty();
                    } else {
                        throw new WebClientResponseException(clientResponse.rawStatusCode(), clientResponse.statusCode().getReasonPhrase(), null, null, null);
                    }
                });
    }
}
