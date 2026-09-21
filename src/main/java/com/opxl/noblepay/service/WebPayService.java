package com.opxl.noblepay.service;

import com.opxl.noblepay.dto.request.AuthorizeInswRequest;
import com.opxl.noblepay.dto.request.InitiateWebPayRequest;
import com.opxl.noblepay.dto.request.InswResendOtpRequest;

interface  WebPayService {

    Object initiatePayment(InitiateWebPayRequest initiateWebPayRequest);
    Object authorizeTrans(AuthorizeInswRequest authorizeInswRequest);
    Object statusVerify(String transactionRef,String amount);
    Object resendOtp(InswResendOtpRequest resendOtpRequest);

}
