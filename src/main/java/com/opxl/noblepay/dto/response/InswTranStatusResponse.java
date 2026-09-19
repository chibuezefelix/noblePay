package com.opxl.noblepay.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class InswTranStatusResponse {
    @SerializedName("TransactionDate")

    private String transactionDate;
    @SerializedName("ResponseDescription")

    private String responseDescription;
    @SerializedName("TerminalId")

    private String terminalId;
    @SerializedName("RetrievalReferenceNumber")

    private String retrievalReferenceNumber;
    @SerializedName("RemittanceAmount")

    private Integer remittanceAmount;
    @SerializedName("MerchantReference")

    private Integer merchantReference;
    @SerializedName("BankCode")

    private String bankCode;
    @SerializedName("PaymentId")

    private Integer paymentId;
    @SerializedName("Stan")

    private String stan;
    @SerializedName("Channel")

    private String channel;
    @SerializedName("Amount")

    private Integer amount;
    @SerializedName("ResponseCode")

    private String responseCode;
    @SerializedName("paymentReference")

    private String paymentReference;
    @SerializedName("CardNumber")
    private String cardNumber;
    @SerializedName("SplitAccounts")

    private List<Object> splitAccounts;
}
