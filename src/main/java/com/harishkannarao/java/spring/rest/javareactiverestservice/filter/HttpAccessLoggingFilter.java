package com.harishkannarao.java.spring.rest.javareactiverestservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static net.logstash.logback.argument.StructuredArguments.fields;
import static org.springframework.web.server.ServerWebExchange.LOG_ID_ATTRIBUTE;

@SuppressWarnings({"NullableProblems", "unused"})
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class HttpAccessLoggingFilter implements WebFilter {
    public static final String REQUEST_START_TIME_ATTRIBUTE = "REQUEST_START_TIME";
    public static final String REQUEST_TIMESTAMP_ATTRIBUTE = "REQUEST_TIMESTAMP";

    private final Logger logger = LoggerFactory.getLogger(HttpAccessLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getAttributes().put(REQUEST_START_TIME_ATTRIBUTE, System.currentTimeMillis());
        exchange.getAttributes().put(REQUEST_TIMESTAMP_ATTRIBUTE, OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS).format(DateTimeFormatter.ISO_DATE_TIME));
        exchange.getResponse().beforeCommit(() -> {
            AccessLogEntries accessLogEntries = new AccessLogEntries(
                    exchange.getResponse().getRawStatusCode(),
                    exchange.getRequiredAttribute(LOG_ID_ATTRIBUTE),
                    exchange.getRequest().getMethodValue(),
                    exchange.getRequest().getPath().pathWithinApplication().value(),
                    exchange.getRequest().getQueryParams().toString(),
                    System.currentTimeMillis() - (Long) exchange.getRequiredAttribute(REQUEST_START_TIME_ATTRIBUTE),
                    exchange.getRequiredAttribute(REQUEST_TIMESTAMP_ATTRIBUTE),
                    OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS).format(DateTimeFormatter.ISO_DATE_TIME)
            );
            logger.info("{}", fields(accessLogEntries));
            return Mono.empty();
        });
        return chain.filter(exchange);
    }

    public record AccessLogEntries(
            Integer status,
            String requestId,
            String method,
            String path,
            String queryParams,
            Long responseTimeMs,
            String requestTimestamp,
            String responseTimestamp
    ) { }

}
