package com.brunolopes.restmodule.response;

import java.math.BigDecimal;

public class CalculationResult {
    private BigDecimal result;

    public CalculationResult(BigDecimal result) {
        this.result = result;
    }

    public CalculationResult(){}

    public BigDecimal getResult() {
        return result;
    }
    public void setResult(BigDecimal result) {
        this.result = result;
    }
}
