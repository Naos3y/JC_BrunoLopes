package com.brunolopes.restmodule.controller;

import com.brunolopes.restmodule.exception.CalculationTimeoutException;
import com.brunolopes.restmodule.request.CalculationRequest;
import com.brunolopes.restmodule.response.CalculationResponse;
import com.brunolopes.restmodule.response.CalculationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {
    @Mock
    private KafkaTemplate<String, CalculationRequest> kafkaTemplate;

    @InjectMocks
    private CalculatorController calculatorController;

    @Test
    void sum_ShouldReturnResult_WhenCalculationSucceeds() throws Exception {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");
        BigDecimal expectedResult = new BigDecimal("15");

        // Mock Kafka send and capture request
        doAnswer(invocation -> {
            CalculationRequest request = invocation.getArgument(2);
            // Simulate async response
            new Thread(() -> {
                CalculationResponse response = new CalculationResponse(request.getRequestID(), expectedResult);
                calculatorController.handleResult(response);
            }).start();
            return CompletableFuture.completedFuture(null);
        }).when(kafkaTemplate).send(anyString(), anyString(), any(CalculationRequest.class));

        ResponseEntity<CalculationResult> response = calculatorController.sum(a, b);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody().getResult());
    }

    @Test
    void subtract_ShouldReturnResult_WhenCalculationSucceeds() throws Exception {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");
        BigDecimal expectedResult = new BigDecimal("5");

        doAnswer(invocation -> {
            CalculationRequest request = invocation.getArgument(2);
            new Thread(() -> {
                CalculationResponse response = new CalculationResponse(request.getRequestID(), expectedResult);
                calculatorController.handleResult(response);
            }).start();
            return CompletableFuture.completedFuture(null);
        }).when(kafkaTemplate).send(anyString(), anyString(), any(CalculationRequest.class));

        ResponseEntity<CalculationResult> response = calculatorController.subtract(a, b);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody().getResult());
    }

    @Test
    void multiply_ShouldReturnResult_WhenCalculationSucceeds() throws Exception {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");
        BigDecimal expectedResult = new BigDecimal("50");

        doAnswer(invocation -> {
            CalculationRequest request = invocation.getArgument(2);
            new Thread(() -> {
                CalculationResponse response = new CalculationResponse(request.getRequestID(), expectedResult);
                calculatorController.handleResult(response);
            }).start();
            return CompletableFuture.completedFuture(null);
        }).when(kafkaTemplate).send(anyString(), anyString(), any(CalculationRequest.class));

        ResponseEntity<CalculationResult> response = calculatorController.multiply(a, b);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody().getResult());
    }

    @Test
    void divide_ShouldReturnResult_WhenCalculationSucceeds() throws Exception {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");
        BigDecimal expectedResult = new BigDecimal("2.00");

        doAnswer(invocation -> {
            CalculationRequest request = invocation.getArgument(2);
            new Thread(() -> {
                CalculationResponse response = new CalculationResponse(request.getRequestID(), expectedResult);
                calculatorController.handleResult(response);
            }).start();
            return CompletableFuture.completedFuture(null);
        }).when(kafkaTemplate).send(anyString(), anyString(), any(CalculationRequest.class));

        ResponseEntity<?> response = calculatorController.divide(a, b);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, ((CalculationResult)response.getBody()).getResult());
    }

    @Test
    void sum_ShouldThrowException_WhenTimeout() {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");

        when(kafkaTemplate.send(anyString(), anyString(), any(CalculationRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        assertThrows(CalculationTimeoutException.class, () -> calculatorController.sum(a, b));
    }
}