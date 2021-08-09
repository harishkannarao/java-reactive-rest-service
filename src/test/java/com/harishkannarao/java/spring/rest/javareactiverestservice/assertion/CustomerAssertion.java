package com.harishkannarao.java.spring.rest.javareactiverestservice.assertion;

import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerAssertion {

    public static void assertEquals(Customer actual, Customer expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
    }
}
