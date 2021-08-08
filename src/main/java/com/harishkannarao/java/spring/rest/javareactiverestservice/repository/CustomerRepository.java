package com.harishkannarao.java.spring.rest.javareactiverestservice.repository;

import com.harishkannarao.java.spring.rest.javareactiverestservice.json.JsonUtil;
import com.harishkannarao.java.spring.rest.javareactiverestservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CustomerRepository {

    private static final String CREATE_CUSTOMER_SQL = "insert into customer_table (data) values (cast(:json_data as jsonb)) returning data";
    private static final String GET_CUSTOMER_BY_ID_SQL = "select data from customer_table where (cast(data->>'id' as uuid)) = :id";

    private final DatabaseClient databaseClient;
    private final JsonUtil jsonUtil;

    @Autowired
    public CustomerRepository(DatabaseClient databaseClient, JsonUtil jsonUtil) {
        this.databaseClient = databaseClient;
        this.jsonUtil = jsonUtil;
    }

    public Mono<UUID> createCustomer(Customer customer) {
        String json_data = jsonUtil.toJson(customer);
        return databaseClient.sql(CREATE_CUSTOMER_SQL)
                .bind("json_data", json_data)
                .map(row -> jsonUtil.fromJson(row.get("data", String.class), Customer.class))
                .first()
                .map(Customer::getId);
    }

    public Mono<Customer> getCustomer(UUID id) {
        return databaseClient.sql(GET_CUSTOMER_BY_ID_SQL)
                .bind("id", id)
                .map(row -> jsonUtil.fromJson(row.get("data", String.class), Customer.class))
                .first();
    }
}
