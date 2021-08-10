package com.harishkannarao.java.spring.rest.javareactiverestservice.json;

import com.harishkannarao.java.spring.rest.javareactiverestservice.assertion.CustomerAssertion;
import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.integration.AbstractBaseIntegrationTest;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class JsonUtilIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void writeAndReadObjectAsJson() {
        Customer input = CustomerFixtures.randomCustomer();
        JsonUtil underTest = getBean(JsonUtil.class);
        String json = underTest.toJson(input);
        Customer result = underTest.fromJson(json, Customer.class);
        CustomerAssertion.assertEquals(result, input);
    }

    @Test
    void writeAndReadCollectionAsJson() {
        Customer input1 = CustomerFixtures.randomCustomer();
        Customer input2 = CustomerFixtures.randomCustomer();
        List<Customer> inputList = List.of(input1, input2);
        JsonUtil underTest = getBean(JsonUtil.class);
        String json = underTest.toJson(inputList);
        List<Customer> result = Arrays.asList(underTest.fromJson(json, Customer[].class));
        CustomerAssertion.assertEquals(result.get(0), input1);
        CustomerAssertion.assertEquals(result.get(1), input2);
    }
}
