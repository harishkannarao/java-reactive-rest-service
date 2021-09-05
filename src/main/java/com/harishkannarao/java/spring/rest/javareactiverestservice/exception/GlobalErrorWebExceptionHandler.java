package com.harishkannarao.java.spring.rest.javareactiverestservice.exception;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;

import java.util.HashMap;
import java.util.Map;

public class GlobalErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> originalErrorAttributes = super.getErrorAttributes(request, options);
        Throwable error = getError(request);
        return transformErrorAttributes(originalErrorAttributes, error);
    }

    private Map<String, Object> transformErrorAttributes(Map<String, Object> original, Throwable error) {
        Map<String, Object> transformed = new HashMap<>(original);
        if (error instanceof DataIntegrityViolationException) {
            DataIntegrityViolationException exception = (DataIntegrityViolationException) error;
            transformed.put("status", HttpStatus.CONFLICT.value());
            transformed.put("error", HttpStatus.CONFLICT.getReasonPhrase());
            transformed.put("message", exception.getCause().getMessage());
        }
        if (error.getCause() instanceof ServerWebInputException) {
            ServerWebInputException exception = (ServerWebInputException) error.getCause();
            transformed.put("status", exception.getStatus().value());
            transformed.put("error", exception.getStatus().getReasonPhrase());
            transformed.put("message", exception.getMessage());
        }
        return transformed;
    }
}
