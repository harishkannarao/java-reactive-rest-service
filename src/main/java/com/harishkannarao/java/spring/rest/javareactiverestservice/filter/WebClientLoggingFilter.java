package com.harishkannarao.java.spring.rest.javareactiverestservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Objects;
import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.fields;

@Component
public class WebClientLoggingFilter implements ExchangeFilterFunction {
    private static final String REQUEST_START_TIME = WebClientLoggingFilter.class.getName() + ".START_TIME";
    private static final String REQUEST_PATH = WebClientLoggingFilter.class.getName() + ".PATH";
    private static final String REQUEST_QUERY = WebClientLoggingFilter.class.getName() + ".QUERY";
    public static final String REQUEST_ID = WebClientLoggingFilter.class.getName() + ".REQUEST_ID";

    private final Logger logger = LoggerFactory.getLogger(WebClientLoggingFilter.class);

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request)
                .contextWrite((context) -> context
                        .delete(REQUEST_START_TIME)
                        .delete(REQUEST_PATH)
                        .delete(REQUEST_QUERY)
                        .delete(REQUEST_ID))
                .doOnEach((signal) -> {
                    if (!signal.isOnComplete()) {
                        Long startTime = signal.getContextView().get(REQUEST_START_TIME);
                        String path = signal.getContextView().get(REQUEST_PATH);
                        String query = null;
                        if (signal.getContextView().hasKey(REQUEST_QUERY)) {
                            query = signal.getContextView().get(REQUEST_QUERY);
                        }
                        Optional<ClientResponse> clientResponse = Optional.ofNullable(signal.get());
                        Integer status = clientResponse.map(ClientResponse::statusCode).map(HttpStatus::value).orElse(null);
                        long responseTime = System.currentTimeMillis() - startTime;
                        LogEntries logEntries = new LogEntries(status, responseTime, path, query, signal.getContextView().get(REQUEST_ID));
                        logger.info("{}", fields(logEntries));
                    }
                })
                .contextWrite((context) -> {
                    Context updated = context;
                    updated = updated.put(REQUEST_START_TIME, System.currentTimeMillis());
                    updated = updated.put(REQUEST_PATH, request.url().getPath());
                    if(Objects.nonNull(request.url().getQuery())){
                        updated = updated.put(REQUEST_QUERY, request.url().getQuery());
                    }
                    Optional<Object> optionalRequestId = request.attribute(REQUEST_ID);
                    if (optionalRequestId.isPresent()) {
                        updated = updated.put(REQUEST_ID, optionalRequestId.get());
                    }
                    return updated;
                });
    }

    public static class LogEntries {
        private final Integer status;
        private final Long responseTimeMs;
        private final String path;
        private final String query;
        private final String requestId;

        public LogEntries(Integer status, Long responseTimeMs, String path, String query, String requestId) {
            this.status = status;
            this.responseTimeMs = responseTimeMs;
            this.path = path;
            this.query = query;
            this.requestId = requestId;
        }

        public Long getResponseTimeMs() {
            return responseTimeMs;
        }

        public Integer getStatus() {
            return status;
        }

        public String getRequestId() {
            return requestId;
        }

        public String getPath() {
            return path;
        }

        public String getQuery() {
            return query;
        }

        @Override
        public String toString() {
            return "LogEntries{" +
                    "status=" + status +
                    ", responseTimeMs=" + responseTimeMs +
                    ", path='" + path + '\'' +
                    ", query='" + query + '\'' +
                    ", requestId='" + requestId + '\'' +
                    '}';
        }
    }
}
