package com.harishkannarao.java.spring.rest.javareactiverestservice.stub;

import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Order;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.BodyWithContentType;
import org.mockserver.model.MediaType;

import java.util.List;
import java.util.UUID;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class OrderServiceStub {
    private final MockServerClient mockServerClient;

    public OrderServiceStub(MockServerClient mockServerClient) {
        this.mockServerClient = mockServerClient;
    }

    public void stubCustomerOrders(int status, String responseBody, String customerId) {
        mockServerClient.when(
                        request()
                                .withMethod("GET")
                                .withPath("/order")
                                .withQueryStringParameter("customer", customerId)
                )
                .respond(
                        response()
                                .withStatusCode(status)
                                .withBody(responseBody, MediaType.APPLICATION_JSON)
                );
    }
}
