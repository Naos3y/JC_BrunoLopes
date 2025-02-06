package com.brunolopes.restmodule.request;

import java.math.BigDecimal;

public class CalculationRequest {
    private String requestID;
    private BigDecimal a;
    private BigDecimal b;
    private String operation;

    public CalculationRequest(String requestID, BigDecimal a, BigDecimal b, String operation) {
        this.requestID = requestID;
        this.a = a;
        this.b = b;
        this.operation = operation;
    }

    public String getRequestID() {
        return requestID;
    }
    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
    public BigDecimal getA() {
        return a;
    }
    public void setA(BigDecimal a) {
        this.a = a;
    }
    public BigDecimal getB() {
        return b;
    }
    public void setB(BigDecimal b) {
        this.b = b;
    }
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
}
