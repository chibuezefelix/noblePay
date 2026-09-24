package com.opxl.noblepay.error.exception;

import com.opxl.noblepay.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> onValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(ApiResponse.error("VALIDATION_ERROR", detail));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> onMalformedJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error("MALFORMED_REQUEST", "Request body is not valid JSON"));
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> onNotFound(TransactionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("TXN_NOT_FOUND", "Transaction not found"));
    }

    @ExceptionHandler(OtpResendLimitExceededException.class)
    public ResponseEntity<ApiResponse<Void>> onRateLimit(OtpResendLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header(HttpHeaders.RETRY_AFTER, String.valueOf(ex.getRetryAfterSeconds()))
                .body(ApiResponse.error("RATE_LIMITED", "Too many OTP requests. Please try again shortly."));
    }

    @ExceptionHandler(ProcessorUnavailableException.class)
    public ResponseEntity<ApiResponse<Void>> onUpstream(ProcessorUnavailableException ex) {
        log.error("Processor unavailable during OTP resend", ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error("PROCESSOR_UNAVAILABLE", "Payment provider is temporarily unavailable"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> onUnexpected(Exception ex) {
        log.error("Unhandled error on OTP resend", ex);   // stack trace to logs, never to the client
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error("INTERNAL_ERROR", "An unexpected error occurred"));
    }
}