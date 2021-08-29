package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.client.OrderClient;
import org.junit.jupiter.api.Test;

public class OrderClientIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void getCustomerOrders() {
        OrderClient underTest = getBean(OrderClient.class);
    }

}
