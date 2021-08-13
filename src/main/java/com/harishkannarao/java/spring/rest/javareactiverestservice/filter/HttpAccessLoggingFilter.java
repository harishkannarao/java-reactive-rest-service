package com.harishkannarao.java.spring.rest.javareactiverestservice.filter;

import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static net.logstash.logback.argument.StructuredArguments.entries;
import static org.springframework.web.server.ServerWebExchange.LOG_ID_ATTRIBUTE;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class HttpAccessLoggingFilter implements WebFilter {
    public static final String REQUEST_START_TIME_ATTRIBUTE = "REQUEST_START_TIME";

    private final Logger logger = LoggerFactory.getLogger(HttpAccessLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getAttributes().put(REQUEST_START_TIME_ATTRIBUTE, System.currentTimeMillis());
        exchange.getResponse().beforeCommit(() -> {
            Integer statusCode = exchange.getResponse().getRawStatusCode();
            String requestId = exchange.getRequiredAttribute(LOG_ID_ATTRIBUTE);
            String method = exchange.getRequest().getMethodValue();
            String pathWithApplication = exchange.getRequest().getPath().pathWithinApplication().value();
            String queryParams = exchange.getRequest().getQueryParams().toString();
            long responseTime = System.currentTimeMillis() - (Long) exchange.getRequiredAttribute(REQUEST_START_TIME_ATTRIBUTE);
            Map<String, ? extends Serializable> logEntries = ofEntries(
                    entry("status", statusCode),
                    entry("requestId", requestId),
                    entry("method", method),
                    entry("path", pathWithApplication),
                    entry("queryParams", queryParams),
                    entry("responseTimeInMillis", responseTime)
            );
            logger.info("Access Log {}", entries(logEntries));
            return Mono.empty();
        });
        return chain.filter(exchange);
    }

}
