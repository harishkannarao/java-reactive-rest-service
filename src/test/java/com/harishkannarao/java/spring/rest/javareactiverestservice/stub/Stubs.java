package com.harishkannarao.java.spring.rest.javareactiverestservice.stub;

import com.harishkannarao.java.spring.rest.javareactiverestservice.runner.MockServerTestRunner;

public class Stubs {

    public static OrderServiceStub orderServiceStub() {
        return new OrderServiceStub(MockServerTestRunner.getClient());
    }
}
