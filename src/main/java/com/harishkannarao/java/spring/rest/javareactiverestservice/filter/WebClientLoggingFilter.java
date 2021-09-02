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

import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.fields;

@Component
public class WebClientLoggingFilter implements ExchangeFilterFunction {
    public static final String REQUEST_START_TIME = WebClientLoggingFilter.class.getName() + ".START_TIME";
    public static final String REQUEST_ID = WebClientLoggingFilter.class.getName() + ".REQUEST_ID";

    private final Logger logger = LoggerFactory.getLogger(WebClientLoggingFilter.class);

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request)
                .contextWrite((context) -> context.delete(REQUEST_START_TIME).delete(REQUEST_ID))
                .doOnEach((signal) -> {
                    if (!signal.isOnComplete()) {
                        Long startTime = signal.getContextView().get(REQUEST_START_TIME);
                        Optional<ClientResponse> clientResponse = Optional.ofNullable(signal.get());
                        Integer status = clientResponse.map(ClientResponse::statusCode).map(HttpStatus::value).orElse(null);
                        long responseTime = System.currentTimeMillis() - startTime;
                        LogEntries logEntries = new LogEntries(status, responseTime, signal.getContextView().get(REQUEST_ID));
                        logger.info("{}", fields(logEntries));
                    }
                })
                .contextWrite((context) -> {
                    Context updated = context;
                    updated = updated.put(REQUEST_START_TIME, System.currentTimeMillis());
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
        private final String requestId;

        public LogEntries(Integer status, Long responseTimeMs, String requestId) {
            this.status = status;
            this.responseTimeMs = responseTimeMs;
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

        @Override
        public String toString() {
            return "LogEntries{" +
                    "status=" + status +
                    ", responseTimeMs=" + responseTimeMs +
                    ", requestId='" + requestId + '\'' +
                    '}';
        }
    }
}
