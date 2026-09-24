package com.opxl.noblepay.error.exception;

import lombok.Getter;

public class OtpResendLimitExceededException extends PaymentException {

    @Getter
    private final long retryAfterSeconds;

    public OtpResendLimitExceededException(long retryAfterSeconds) {
        super("RATE_LIMITED", "OTP resend limit exceeded");
        this.retryAfterSeconds = retryAfterSeconds;
    }
}