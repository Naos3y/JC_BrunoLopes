package com.brunolopes.restmodule.response;

import java.math.BigDecimal;

public class CalculationResponse {
    private String requestId;
    private BigDecimal result;

    public CalculationResponse(String requestId, BigDecimal result) {
        this.requestId = requestId;
        this.result = result;
    }

    public CalculationResponse(){}

    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public BigDecimal getResult() {
        return result;
    }
    public void setResult(BigDecimal result) {
        this.result = result;
    }
}
