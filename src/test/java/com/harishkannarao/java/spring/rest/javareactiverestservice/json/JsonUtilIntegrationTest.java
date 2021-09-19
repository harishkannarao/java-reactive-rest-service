package com.harishkannarao.java.spring.rest.javareactiverestservice.json;

import com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures;
import com.harishkannarao.java.spring.rest.javareactiverestservice.integration.AbstractBaseIntegrationTest;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void writeAndReadObjectAsJson() {
        Customer input = CustomerFixtures.randomCustomer();
        JsonUtil underTest = getBean(JsonUtil.class);
        String json = underTest.toJson(input);
        Customer result = underTest.fromJson(json, Customer.class);
        assertThat(result).isEqualTo(input);
    }

    @Test
    void writeAndReadCollectionAsJson() {
        Customer input1 = CustomerFixtures.randomCustomer();
        Customer input2 = CustomerFixtures.randomCustomer();
        List<Customer> inputList = List.of(input1, input2);
        JsonUtil underTest = getBean(JsonUtil.class);
        String json = underTest.toJson(inputList);
        List<Customer> result = Arrays.asList(underTest.fromJson(json, Customer[].class));
        assertThat(result.get(0)).isEqualTo(input1);
        assertThat(result.get(1)).isEqualTo(input2);
    }
}
