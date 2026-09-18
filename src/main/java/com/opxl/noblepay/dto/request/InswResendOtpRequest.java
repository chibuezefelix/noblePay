package com.opxl.noblepay.dto.request;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class InswResendOtpRequest {
    @SerializedName("amount")

    private  String amount;
    @SerializedName("paymentId")

    private  String paymentId;
    @SerializedName("currency")
    private  String currency;
}
