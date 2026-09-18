package com.opxl.noblepay.dto.request;


import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitiateWebPayRequest {

    @SerializedName("expiryDate")
    @NotEmpty
    @NotNull
    @NotBlank
    private String expiryDate;

    @SerializedName("cvv2")
    @NotEmpty
    @NotBlank
    @NotNull
    private String cvv2;

    @SerializedName("pan")
    @NotEmpty
    @NotBlank
    @NotNull
    private String pan;

    @SerializedName("amount")
    @NotEmpty
    @NotBlank
    @NotNull
    private String amount;

    @SerializedName("amount")
    @NotEmpty
    @NotBlank
    @NotNull
    private String currency;

    @SerializedName("customerId")
    @NotEmpty
    @NotBlank
    @NotNull
    private String customerId;

    private String callBackUrl;
    DeviceInformation  deviceInformation;

}
