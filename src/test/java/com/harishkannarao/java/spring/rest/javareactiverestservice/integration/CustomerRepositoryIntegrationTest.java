package com.harishkannarao.java.spring.rest.javareactiverestservice.integration;

import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import com.harishkannarao.java.spring.rest.javareactiverestservice.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.UUID;

import static com.harishkannarao.java.spring.rest.javareactiverestservice.fixture.CustomerFixtures.randomCustomer;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class CustomerRepositoryIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    void createReadListAndDeleteCustomer() {
        Customer input1 = randomCustomer();
        Customer input2 = randomCustomer();
        CustomerRepository underTest = getBean(CustomerRepository.class);

        assertThat(underTest.listCustomers().collectList().block()).hasSize(0);

        UUID id1 = underTest.createCustomer(input1).block();
        assertThat(id1).isEqualTo(input1.id());

        Customer getResult = underTest.getCustomer(input1.id()).block();
        assertThat(getResult).isEqualTo(input1);

        List<Customer> listResult1 = underTest.listCustomers().collectList().block();
        assertThat(listResult1).hasSize(1);
        assertThat(listResult1.get(0)).isEqualTo(input1);

        UUID id2 = underTest.createCustomer(input2).block();
        assertThat(id2).isEqualTo(input2.id());

        List<Customer> listResult2 = underTest.listCustomers().collectList().block();
        assertThat(listResult2).hasSize(2);
        assertThat(listResult2.get(0)).isEqualTo(input1);
        assertThat(listResult2.get(1)).isEqualTo(input2);

        Integer deletedResult = underTest.deleteAllCustomers().block();
        assertThat(deletedResult).isEqualTo(2);
    }

    @Test
    void cannotCreateMultipleCustomersWithSameId() {
        Customer input = randomCustomer();
        CustomerRepository underTest = getBean(CustomerRepository.class);
        UUID createdId = underTest.createCustomer(input).block();
        assertThat(createdId).isEqualTo(input.id());

        DataIntegrityViolationException result = Assertions.assertThrows(DataIntegrityViolationException.class, () -> underTest.createCustomer(input).block());
        assertThat(result).hasMessageContaining("duplicate key value violates unique constraint", "unique_index_jsonb_id");
    }
}
