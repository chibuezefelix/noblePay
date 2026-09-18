package com.opxl.noblepay.dto.request;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceInformation {


    @SerializedName("httpBrowserLanguage")
    private String httpBrowserLanguage;

    @SerializedName("httpBrowserJavaEnabled")
    private Boolean httpBrowserJavaEnabled;

    @SerializedName("httpBrowserColorDepth")
    private String httpBrowserColorDepth;

    @SerializedName("httpBrowserScreenHeight")

    private  String httpBrowserScreenHeight;
    @SerializedName("httpBrowserScreenWidth")

    private  String httpBrowserScreenWidth;
    @SerializedName("httpBrowserTimeDifference")

    private  String httpBrowserTimeDifference;
    @SerializedName("userAgentBrowserValue")

    private  String userAgentBrowserValue;
    @SerializedName("deviceChannel")

    private  String deviceChannel;




}
