package com.opxl.noblepay.dto.response;

import com.google.gson.annotations.SerializedName;

public class InswAuthorizeResponse {
    private String panLast4Digits;
    private String panCode;
    private String tokenExpiryDate;
    private String amount;
    private String transactionIdentifier;
    private String cardType;
    private String transactionRef;
    private String retrievalReferenceNumber;
    private String terminalId;
    private String message;
    private String token;
    private String responseCode;
    private String stan;
}
