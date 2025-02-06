package com.brunolopes.calculatormodule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DivideByZeroException extends RuntimeException{
    public DivideByZeroException(){
        super("Division by zero is not allowed.");
    }
}
