package com.opxl.noblepay.dto.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InswPurchaseResponse {
    private String amount;
    private String paymentId;
    private String transactionRef;
    private String plainTextSupportMessage;
    private String message;
    private String respondCode;
    private String MD;
    private String transactionId;

}
