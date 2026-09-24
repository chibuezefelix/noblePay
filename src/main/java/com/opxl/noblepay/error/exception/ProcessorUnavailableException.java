package com.opxl.noblepay.error.exception;

public class ProcessorUnavailableException extends PaymentException {

    public ProcessorUnavailableException(String message, Throwable cause) {
        super("PROCESSOR_UNAVAILABLE", message, cause);
    }

    public ProcessorUnavailableException(String message) {
        super("PROCESSOR_UNAVAILABLE", message);
    }
}
