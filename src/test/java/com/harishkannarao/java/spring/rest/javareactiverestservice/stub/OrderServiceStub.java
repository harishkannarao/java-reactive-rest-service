package com.harishkannarao.java.spring.rest.javareactiverestservice.stub;

import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.model.RequestDefinition;

import java.util.Optional;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OrderServiceStub {
    private final MockServerClient mockServerClient;

    public OrderServiceStub(MockServerClient mockServerClient) {
        this.mockServerClient = mockServerClient;
    }

    public void stubGetOrders(int status,
                              String responseBody,
                              Optional<String> customerId,
                              Optional<Integer> limit) {
        HttpRequest httpRequest = request()
                .withMethod("GET")
                .withPath("/order");
        customerId.ifPresent(it -> httpRequest.withQueryStringParameter("customer", it));
        limit.ifPresent(it -> httpRequest.withQueryStringParameter("limit", it.toString()));
        HttpResponse httpResponse = response()
                .withStatusCode(status)
                .withBody(responseBody, MediaType.APPLICATION_JSON);
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
}
