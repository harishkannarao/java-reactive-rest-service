package com.harishkannarao.java.spring.rest.javareactiverestservice.stub;

import org.mockserver.client.MockServerClient;
import org.mockserver.model.*;

import java.util.Optional;
import java.util.UUID;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OrderServiceStub {
    private final MockServerClient mockServerClient;

    public OrderServiceStub(MockServerClient mockServerClient) {
        this.mockServerClient = mockServerClient;
    }


    public void stubGetOrder(UUID id, int status, String responseBody) {
        HttpRequest httpRequest = request()
                .withMethod("GET")
                .withPath("/order/" + id);
        HttpResponse httpResponse = response()
                .withStatusCode(status)
                .withBody(responseBody, MediaType.APPLICATION_JSON);
        mockServerClient.when(httpRequest).respond(httpResponse);
    }

    public void stubGetOrders(int status,
                              String responseBody,
                              Optional<Integer> limit,
                              Optional<Delay> delay) {
        HttpRequest httpRequest = request()
                .withMethod("GET")
                .withPath("/order");
        limit.ifPresent(it -> httpRequest.withQueryStringParameter("limit", it.toString()));
        HttpResponse httpResponse = response()
                .withStatusCode(status)
                .withBody(responseBody, MediaType.APPLICATION_JSON);
        delay.ifPresent(httpResponse::withDelay);
        mockServerClient.when(httpRequest)
                .respond(httpResponse);
    }

    public void stubGetOrders(int status, String responseBody) {
        stubGetOrders(status, responseBody, Optional.empty(), Optional.empty());
    }

    public RequestDefinition[] retrieveGetOrderRequests() {
        return mockServerClient.retrieveRecordedRequests(
                request()
                        .withMethod("GET")
                        .withPath("/order")
        );
    }

    public void stubCreateOrder(int status) {
        mockServerClient.when(
                        request()
                                .withMethod("POST")
                                .withPath("/order")
                )
                .respond(
                        response()
                                .withStatusCode(status)
                );
    }

    public RequestDefinition[] retrieveCreateOrderRequests() {
        return mockServerClient.retrieveRecordedRequests(
                request()
                        .withMethod("POST")
                        .withPath("/order")
        );
    }

    public void stubDeleteOrders(int status) {
        mockServerClient.when(
                        request()
                                .withMethod("POST")
                                .withPath("/order/delete")
                )
                .respond(
                        response()
                                .withStatusCode(status)
                );
    }

    public RequestDefinition[] retrieveDeleteOrdersRequests() {
        return mockServerClient.retrieveRecordedRequests(
                request()
                        .withMethod("POST")
                        .withPath("/order/delete")
        );
    }

    public void stubGetCustomerOrders(int status, String customerId, Optional<String> responseBody) {
        HttpResponse httpResponse = response().withStatusCode(status);
        responseBody.ifPresent(value -> httpResponse.withBody(value, MediaType.APPLICATION_JSON));

        mockServerClient.when(
                        request()
                                .withMethod("GET")
                                .withPath("/order/customer/" + customerId)
                )
                .respond(httpResponse);
    }

    public RequestDefinition[] retrieveGetCustomerOrdersRequests(String customerId) {
        return mockServerClient.retrieveRecordedRequests(
                request()
                        .withMethod("GET")
                        .withPath("/order/customer/" + customerId)
        );
    }
}
