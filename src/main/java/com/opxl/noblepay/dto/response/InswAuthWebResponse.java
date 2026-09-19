package com.opxl.noblepay.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class InswAuthWebResponse {
    @SerializedName("access_token")

    private String accessToken;
    @SerializedName("merchant_code")

    private  String merchantCode;
    @SerializedName("requestor_id")

    private  String requestorId;
    @SerializedName("scope")

    private  String scope;
    @SerializedName("token_type")

    private  String tokenType;
    @SerializedName("expiresIn")

    private String expiresIn;
    @SerializedName("client_name")

    private  String clientName;
    @SerializedName("payable_id")

    private  String payableId;
    @SerializedName("jti")

    private  String jti;
}
