package com.opxl.noblepay.service;

import com.opxl.noblepay.dto.request.InitiateWebPayRequest;

interface  WebPayService {

    Object initiatePayment(InitiateWebPayRequest initiateWebPayRequest);

}
