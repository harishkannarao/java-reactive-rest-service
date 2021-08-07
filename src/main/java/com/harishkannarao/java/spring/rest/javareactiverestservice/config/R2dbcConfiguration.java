package com.harishkannarao.java.spring.rest.javareactiverestservice.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

@Configuration
public class R2dbcConfiguration extends AbstractR2dbcConfiguration {

    private final Environment environment;

    public R2dbcConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                .host(environment.getProperty("postgresql-datasource.host"))
                .port(environment.getProperty("postgresql-datasource.port", Integer.class))
                .database(environment.getProperty("postgresql-datasource.database"))
                .username(environment.getProperty("postgresql-datasource.username"))
                .password(environment.getProperty("postgresql-datasource.password"))
                .build()
        );
    }
}
