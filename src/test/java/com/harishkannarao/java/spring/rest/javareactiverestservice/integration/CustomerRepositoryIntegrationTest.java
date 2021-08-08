package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("ConstantConditions")
public class CustomerRepositoryIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void createAndReadCustomer() {
        Customer input = new Customer(
                UUID.randomUUID(),
                "firstName",
                "lastName"
        );
        CustomerRepository underTest = getBean(CustomerRepository.class);
        UUID createdId = underTest.createCustomer(input).block();
        assertThat(createdId).isEqualTo(input.getId());

        Customer result = underTest.getCustomer(input.getId()).block();
        assertThat(result.getId()).isEqualTo(input.getId());
        assertThat(result.getFirstName()).isEqualTo(input.getFirstName());
        assertThat(result.getLastName()).isEqualTo(input.getLastName());
    }

    @Test
    void cannotCreateMultipleCustomersWithSameId() {
        Customer input = new Customer(
                UUID.randomUUID(),
                "firstName",
                "lastName"
        );
        CustomerRepository underTest = getBean(CustomerRepository.class);
        UUID createdId = underTest.createCustomer(input).block();
        assertThat(createdId).isEqualTo(input.getId());

        assertThatThrownBy(() -> underTest.createCustomer(input).block())
                .hasMessageContaining("duplicate key value violates unique constraint \"unique_index_jsonb_id\"");
    }
}