package com.opxl.noblepay.utils;


import com.opxl.noblepay.dto.request.InitiateWebPayRequest;
import io.micrometer.common.util.StringUtils;

public class DataMaskingUtil {
    public static String maskString(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }
        return "*".repeat(input.length());
    }

    public static String maskPan(String pan) {
        if (StringUtils.isEmpty(pan) || pan.length() <= 10) {
            return maskString(pan);
        }

        String firstSix = pan.substring(0, 6);
        String lastFour = pan.substring(pan.length() - 4);
        String middle = "*".repeat(pan.length() - 10);

        return firstSix + middle + lastFour;
    }

    public static String maskCardWebPayData(InitiateWebPayRequest request) {

        return "InitiateIswWebPayRequest{" +
                "expiryDate='" + maskString(request.getExpiryDate()) + '\'' +
                ", cvv2='" + maskString(request.getCvv2()) + '\'' +
                ", pin='" + maskString(request.getPin()) + '\'' +
                ", pan='" + maskPan(request.getPan()) + '\'' +
                ", amount='" + request.getAmount() + '\'' +
                ", currency='" + request.getCurrency() + '\'' +
                ", customerId='" + request.getCustomerId() + '\'' +
                ", callBackUrl='" + request.getCallBackUrl() + '\'' +
                ", deviceInformation=" + request.getDeviceInformation() +
                '}';
    }

}