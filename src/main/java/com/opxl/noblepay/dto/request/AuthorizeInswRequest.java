package com.opxl.noblepay.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthorizeInswRequest {
    private String paymentId;
    private String otp;
    private String transactionId;
    private  String eciFlag;
    private  String authFlag;
    private String md;
}
