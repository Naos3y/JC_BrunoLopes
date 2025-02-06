package com.brunolopes.calculatormodule.service;

import com.brunolopes.calculatormodule.exception.DivideByZeroException;
import com.brunolopes.calculatormodule.exception.InvalidOperationException;
import com.brunolopes.calculatormodule.request.CalculationRequest;
import com.brunolopes.calculatormodule.response.CalculationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

    private final KafkaTemplate<String, CalculationResponse> kafkaTemplate;

    public CalculatorService(KafkaTemplate<String, CalculationResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "calculation-requests", groupId = "calculator-service")
    public void handleCalculation(CalculationRequest request){
        logger.info("Received calculation request: {}", request);

        BigDecimal result = calculateResult(request);
        logger.info("Calculated result: {} for request: {}", result, request.getRequestID());

        CalculationResponse response = new CalculationResponse(request.getRequestID(), result);
        kafkaTemplate.send("calculation-results", request.getRequestID(), response);
        logger.info("Sent result back to REST module");
    }

    private BigDecimal calculateResult(CalculationRequest request) {
        switch (request.getOperation()){
            case "sum":
                return request.getA().add(request.getB());
            case "subtract":
                return request.getA().subtract(request.getB());
            case "multiply":
                return request.getA().multiply(request.getB());
            case "divide":
                if(request.getB().compareTo(BigDecimal.ZERO) == 0)
                    throw new DivideByZeroException();

                return request.getA().divide(request.getB(), 2, RoundingMode.HALF_UP);
            default:
                throw new InvalidOperationException(request.getOperation());
        }
    }
}