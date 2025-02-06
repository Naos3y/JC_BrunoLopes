package com.brunolopes.restmodule.controller;

import com.brunolopes.restmodule.exception.CalculationTimeoutException;
import com.brunolopes.restmodule.request.CalculationRequest;
import com.brunolopes.restmodule.response.CalculationResponse;
import com.brunolopes.restmodule.response.CalculationResult;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@RestController
public class CalculatorController {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;
    private final Map<String, CompletableFuture<BigDecimal>> pendingRequests = new ConcurrentHashMap<>();

    public CalculatorController(KafkaTemplate<String, CalculationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @GetMapping("/sum")
    public ResponseEntity<CalculationResult> sum(@RequestParam("a")BigDecimal a, @RequestParam("b")BigDecimal b){
        String requestId = UUID.randomUUID().toString();
        CalculationRequest calculationRequest = new CalculationRequest(requestId, a, b, "sum");
        logger.info("Received Calculation request <SUM>: {}", calculationRequest);

        CompletableFuture<BigDecimal> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        kafkaTemplate.send("calculation-requests", requestId, calculationRequest);
        logger.info("SUM Request sent to calculator");

        try{
            BigDecimal result = future.get(10, TimeUnit.SECONDS);
            logger.info("SUM Result received from calculator: {} from request: {}", result, requestId);
            return ResponseEntity.ok(new CalculationResult(result));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            logger.info("SUM Timeout exception");
            throw new CalculationTimeoutException(requestId);
        } finally {
            logger.info("SUM Request removed from PendingRequests");
            pendingRequests.remove(requestId);
        }
    }

    @GetMapping("/subtract")
    public ResponseEntity<CalculationResult> subtract(@RequestParam("a")BigDecimal a, @RequestParam("b")BigDecimal b){
        String requestId = UUID.randomUUID().toString();
        CalculationRequest calculationRequest = new CalculationRequest(requestId, a, b, "subtract");
        logger.info("Received Calculation request <SUBTRACT>: {}", calculationRequest);

        CompletableFuture<BigDecimal> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        kafkaTemplate.send("calculation-requests", requestId, calculationRequest);
        logger.info("SUBTRACT Request sent to calculator");

        try{
            BigDecimal result = future.get(10, TimeUnit.SECONDS);
            logger.info("SUBTRACT Result received from calculator: {} from request: {}", result, requestId);
            return ResponseEntity.ok(new CalculationResult(result));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            logger.info("SUBTRACT Timeout exception");
            throw new CalculationTimeoutException(requestId);
        } finally {
            logger.info("SUBTRACT Request removed from PendingRequests");
            pendingRequests.remove(requestId);
        }
    }

    @GetMapping("/multiply")
    public ResponseEntity<CalculationResult> multiply(@RequestParam("a")BigDecimal a, @RequestParam("b")BigDecimal b){
        String requestId = UUID.randomUUID().toString();
        CalculationRequest calculationRequest = new CalculationRequest(requestId, a, b, "multiply");
        logger.info("Received Calculation request <MULTIPLY>: {}", calculationRequest);

        CompletableFuture<BigDecimal> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        kafkaTemplate.send("calculation-requests", requestId, calculationRequest);
        logger.info("MULTIPLY Request sent to calculator");

        try{
            BigDecimal result = future.get(10, TimeUnit.SECONDS);
            logger.info("MULTIPLY Result received from calculator: {} from request: {}", result, requestId);
            return ResponseEntity.ok(new CalculationResult(result));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            logger.info("MULTIPLY Timeout exception");
            throw new CalculationTimeoutException(requestId);
        } finally {
            logger.info("MULTIPLY Request removed from PendingRequests");
            pendingRequests.remove(requestId);
        }
    }

    @GetMapping("/divide")
    public ResponseEntity<?> divide(@RequestParam("a")BigDecimal a, @RequestParam("b")BigDecimal b){
        String requestId = UUID.randomUUID().toString();
        CalculationRequest calculationRequest = new CalculationRequest(requestId, a, b, "divide");
        logger.info("Received Calculation request <DIVIDE>: {}", calculationRequest);

        CompletableFuture<BigDecimal> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        kafkaTemplate.send("calculation-requests", requestId, calculationRequest);
        logger.info("DIVIDE Request sent to calculator");

        try{
            BigDecimal result = future.get(10, TimeUnit.SECONDS);
            logger.info("DIVIDE Result received from calculator: {} from request: {}", result, requestId);
            return ResponseEntity.ok(new CalculationResult(result));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            logger.info("DIVIDE Timeout exception");
            throw new CalculationTimeoutException(requestId);
        } finally {
            logger.info("DIVIDE Request removed from PendingRequests");
            pendingRequests.remove(requestId);
        }
    }

    @KafkaListener(topics = "calculation-results", groupId = "rest-calculator")
    public void handleResult(CalculationResponse result) {
        logger.info("Received result from Kafka: {}", result);
        CompletableFuture<BigDecimal> future = pendingRequests.get(result.getRequestId());

        if (future != null) {
            future.complete(result.getResult());
            logger.info("Result processed for request: {}", result.getRequestId());
        } else {
            logger.warn("No pending request found for ID: {}", result.getRequestId());
        }
    }
}
