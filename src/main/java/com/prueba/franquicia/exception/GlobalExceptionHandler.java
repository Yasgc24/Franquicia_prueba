package com.prueba.franquicia.exception;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        
        HttpStatus status = determineHttpStatus(ex);
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        ErrorResponse errorResponse = new ErrorResponse(status.value(), ex.getMessage());
        
        DataBuffer dataBuffer;
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            dataBuffer = bufferFactory.wrap(bytes);
        } catch (JsonProcessingException e) {
            String fallbackMessage = "{\"mensaje\":\"Error interno del servidor\"}";
            dataBuffer = bufferFactory.wrap(fallbackMessage.getBytes());
        }
        
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
    
    private HttpStatus determineHttpStatus(Throwable ex) {
        if (ex instanceof ResourceNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof BadRequestException) {
            return HttpStatus.BAD_REQUEST;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}