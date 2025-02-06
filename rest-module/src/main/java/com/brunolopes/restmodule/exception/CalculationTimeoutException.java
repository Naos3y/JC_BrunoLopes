package com.brunolopes.restmodule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
public class CalculationTimeoutException extends RuntimeException {
    public CalculationTimeoutException(String requestId) {
        super("Calculation timed out for request: " + requestId);
    }
}
