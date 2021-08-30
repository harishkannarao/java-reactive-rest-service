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

    public void stubOrders(int status, String responseBody, Optional<String> customerId) {
        HttpRequest httpRequest = request()
                .withMethod("GET")
                .withPath("/order");
        customerId.ifPresent(it -> httpRequest.withQueryStringParameter("customer", it));
        HttpResponse httpResponse = response()
                .withStatusCode(status)
                .withBody(responseBody, MediaType.APPLICATION_JSON);
        mockServerClient.when(httpRequest)
                .respond(httpResponse);
    }

    public void stubOrders(int status, String responseBody) {
        stubOrders(status, responseBody, Optional.empty());
    }

    public RequestDefinition[] getOrderRequests() {
        return mockServerClient.retrieveRecordedRequests(
                request()
                        .withMethod("GET")
                        .withPath("/order")
        );
    }
}
