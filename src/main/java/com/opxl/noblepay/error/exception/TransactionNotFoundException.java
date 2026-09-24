package com.opxl.noblepay.error.exception;

public class TransactionNotFoundException extends PaymentException {

    public TransactionNotFoundException(String transactionRef) {
        super("TXN_NOT_FOUND", "Transaction not found: " + transactionRef);
    }
}
