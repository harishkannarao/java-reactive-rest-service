package com.harishkannarao.java.spring.rest.javareactiverestservice.stub;

import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;
import org.mockserver.model.RequestDefinition;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class OrderServiceStub {
    private final MockServerClient mockServerClient;

    public OrderServiceStub(MockServerClient mockServerClient) {
        this.mockServerClient = mockServerClient;
    }

    public void stubOrders(int status, String responseBody) {
        mockServerClient.when(
                        request()
                                .withMethod("GET")
                                .withPath("/order")
                )
                .respond(
                        response()
                                .withStatusCode(status)
                                .withBody(responseBody, MediaType.APPLICATION_JSON)
                );
    }

    public RequestDefinition[] getOrderRequests() {
        return mockServerClient.retrieveRecordedRequests(
                request()
                        .withMethod("GET")
                        .withPath("/order")
        );
    }
}
