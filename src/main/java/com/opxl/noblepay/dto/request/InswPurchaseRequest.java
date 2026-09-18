package com.opxl.noblepay.dto.request;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InswPurchaseRequest {
    String amount;
    String authData;
    String customerId;
    String transactionRef;
    String currency;
    String callBackUrl;
    DeviceInformation deviceInformation;
}
