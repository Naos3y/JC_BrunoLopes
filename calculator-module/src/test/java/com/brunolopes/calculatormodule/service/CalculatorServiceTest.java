package com.brunolopes.calculatormodule.service;

import com.brunolopes.calculatormodule.exception.DivideByZeroException;
import com.brunolopes.calculatormodule.exception.InvalidOperationException;
import com.brunolopes.calculatormodule.request.CalculationRequest;
import com.brunolopes.calculatormodule.response.CalculationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceTest {


    @Mock
    private KafkaTemplate<String, CalculationResponse> kafkaTemplate;

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        calculatorService = new CalculatorService(kafkaTemplate);
    }

    @Test
    void testSumOperation() {
        CalculationRequest request = new CalculationRequest(
                "test-sum-id",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5),
                "sum"
        );

        calculatorService.handleCalculation(request);

        verify(kafkaTemplate).send(
                eq("calculation-results"),
                eq("test-sum-id"),
                argThat(response ->
                        response.getRequestId().equals("test-sum-id") &&
                                response.getResult().equals(BigDecimal.valueOf(15))
                )
        );
    }

    @Test
    void testSubtractOperation() {
        CalculationRequest request = new CalculationRequest(
                "test-subtract-id",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5),
                "subtract"
        );

        calculatorService.handleCalculation(request);

        verify(kafkaTemplate).send(
                eq("calculation-results"),
                eq("test-subtract-id"),
                argThat(response ->
                        response.getRequestId().equals("test-subtract-id") &&
                                response.getResult().equals(BigDecimal.valueOf(5))
                )
        );
    }

    @Test
    void testMultiplyOperation() {
        CalculationRequest request = new CalculationRequest(
                "test-multiply-id",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5),
                "multiply"
        );

        calculatorService.handleCalculation(request);

        verify(kafkaTemplate).send(
                eq("calculation-results"),
                eq("test-multiply-id"),
                argThat(response ->
                        response.getRequestId().equals("test-multiply-id") &&
                                response.getResult().equals(BigDecimal.valueOf(50))
                )
        );
    }

    @Test
    void testDivideOperation() {
        CalculationRequest request = new CalculationRequest(
                "test-divide-id",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5),
                "divide"
        );

        calculatorService.handleCalculation(request);

        verify(kafkaTemplate).send(
                eq("calculation-results"),
                eq("test-divide-id"),
                argThat(response ->
                        response.getRequestId().equals("test-divide-id") &&
                                response.getResult().equals(BigDecimal.valueOf(2.00).setScale(2, RoundingMode.HALF_UP))
                )
        );
    }

    @Test
    void testDivideByZeroThrowsException() {
        CalculationRequest request = new CalculationRequest(
                "test-divide-by-zero-id",
                BigDecimal.valueOf(10),
                BigDecimal.ZERO,
                "divide"
        );

        assertThrows(DivideByZeroException.class, () ->
                calculatorService.handleCalculation(request)
        );
    }

    @Test
    void testInvalidOperationThrowsException() {
        CalculationRequest request = new CalculationRequest(
                "test-invalid-op-id",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5),
                "invalid-op"
        );

        assertThrows(InvalidOperationException.class, () ->
                calculatorService.handleCalculation(request)
        );
    }

}
