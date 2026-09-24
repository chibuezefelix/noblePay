package com.opxl.noblepay.error.exception;



import lombok.Getter;

@Getter
public abstract class PaymentException extends RuntimeException {

    private final String code;

    protected PaymentException(String code, String message) {
        super(message);
        this.code = code;
    }

    protected PaymentException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}